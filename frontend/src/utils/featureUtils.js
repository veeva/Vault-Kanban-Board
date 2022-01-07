import * as lodashCloneDeep from 'lodash.clonedeep';
import * as lodashSortBy from 'lodash.sortby';

export const extractFeatures = (existingFeatures, capacity, featuresData, release) => {
  const features = lodashCloneDeep(existingFeatures);

  const currentReleaseFeatures = [];
  let currentSprintCount = 0;
  const currentReleaseFeatureData = lodashSortBy(
    featuresData.filter(
      (feature) => feature.vaultModelData['feature__cr.proposed_release1__c'] === release
    ),
    'vaultModelData.dependency_stack_rank__c'
  );

  currentReleaseFeatureData.forEach((feature, idx) => {
    const featureData = feature.vaultModelData;
    const id = featureData['feature__cr.id'];
    const name = featureData['feature__cr.name__v'];
    const rank = idx + 1;
    const devSprints = featureData.dev_sprints__c || 0;
    currentSprintCount += devSprints;

    currentReleaseFeatures.push({
      id,
      name,
      rank,
      devSprints,
      release,
      featureType: featureData['feature_type__cr.name__v'],
      committed: featureData.resources_committed__c,
      comments: featureData.comments__c,
      devTeamDependencyId: featureData.id,
      devTeamDependencyName: featureData.name__v,
      aboveOrBelowLine: currentSprintCount <= capacity ? 'Above' : 'Below',
      updated: false,
    });
  });

  features[release] = {
    features: currentReleaseFeatures,
    deleted: [],
  };

  return features;
};

export const calculateFeatureMove = (features, capacity, source, destination, featureId) => {
  const updatedFeatures = lodashCloneDeep(features);
  const destinationFeatures = lodashCloneDeep(updatedFeatures[destination.droppableId].features);
  let sourceFeatures = lodashCloneDeep(updatedFeatures[source.droppableId].features);
  // If droppableId of destination differs from droppable id of source then moving across releases
  // therefore we must rerank source release as well
  const sourceFeatureOrder = sourceFeatures.map((feature) => feature.id);
  const destinationFeatureOrder = destinationFeatures.map((feature) => feature.id);

  let destinationAndSourceReleases;
  if (source.droppableId !== destination.droppableId) {
    destinationAndSourceReleases = [destination.droppableId, source.droppableId];
    sourceFeatureOrder.splice(source.index, 1);
    destinationFeatureOrder.splice(destination.index, 0, featureId);
    const movingFeature = sourceFeatures.find((feature) => feature.id === featureId);
    // Remove from source release features array and move to destination
    sourceFeatures = sourceFeatures.filter((feature) => feature.id !== featureId);
    movingFeature.release = destination.droppableId;
    destinationFeatures.splice(destination.index, 0, movingFeature);
  } else {
    destinationAndSourceReleases = [destination.droppableId];
    sourceFeatureOrder.splice(source.index, 1);
    sourceFeatureOrder.splice(destination.index, 0, featureId);
  }

  destinationAndSourceReleases.forEach((release) => {
    let currentSprintCount = 0;
    let currentFeatureOrder;
    let currentReleaseFeatures;
    if (release === source.droppableId) {
      currentFeatureOrder = sourceFeatureOrder;
      currentReleaseFeatures = sourceFeatures;
    } else {
      currentFeatureOrder = destinationFeatureOrder;
      currentReleaseFeatures = destinationFeatures;
    }

    currentReleaseFeatures = currentReleaseFeatures
      .sort((a, b) => currentFeatureOrder.indexOf(a.id) - currentFeatureOrder.indexOf(b.id))
      .map((feature) => {
        // Updates feature rank if index in feature order array differs from feature.rank value
        const updatedFeature = lodashCloneDeep(feature);

        // + 1 on index as arrays start at 0 but ranks start at 1
        const index = currentFeatureOrder.indexOf(updatedFeature.id) + 1;
        if (index !== updatedFeature.rank) {
          updatedFeature.rank = index;
        }

        // Recalculates above or below line for each feature
        currentSprintCount += updatedFeature.devSprints;
        updatedFeature.aboveOrBelowLine = currentSprintCount <= capacity ? 'Above' : 'Below';
        updatedFeature.updated = true;
        return updatedFeature;
      });

    updatedFeatures[release].features = currentReleaseFeatures;
  });
  return updatedFeatures;
};
