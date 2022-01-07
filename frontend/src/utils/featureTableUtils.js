import * as lodashSortBy from 'lodash.sortby';

const extractFeatures = (featuresData, release) => {
  let features = [];
  featuresData.forEach((feature) => {
    const featureData = feature.vaultModelData;
    const id = featureData['feature__cr.id'];
    const name = featureData['feature__cr.name__v'];
    const rank = featureData.dependency_stack_rank__c;
    const devSprints = featureData.dev_sprints__c;
    features.push({
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
    });
  });
  features = lodashSortBy(features, 'rank');
  return features;
};

export default extractFeatures;
