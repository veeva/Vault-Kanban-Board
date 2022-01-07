import { Flex, Heading, Spacer, Text } from '@chakra-ui/react';
import PropTypes from 'prop-types';
import React from 'react';

const TitleBar = ({ capacity, requestedCapacity, committedCapacity, teamName, isKanbanMode }) => {
  const committedCapacityColor = capacity >= committedCapacity ? 'green' : 'red';

  return (
    <Flex>
      <Heading fontSize="32px">{teamName}</Heading>
      <Spacer />
      {capacity && (
        <>
          <Heading>{capacity}</Heading>
          <Text marginTop="16px" marginRight="32px" marginLeft="4px" fontWeight="bold">
            capacity
          </Text>
        </>
      )}
      {!isKanbanMode && requestedCapacity !== null && typeof requestedCapacity !== 'undefined' && (
        <>
          <Heading>{requestedCapacity}</Heading>
          <Text marginTop="16px" marginRight="32px" marginLeft="4px" fontWeight="bold">
            requested
          </Text>
        </>
      )}
      {!isKanbanMode && committedCapacity !== null && typeof committedCapacity !== 'undefined' && (
        <>
          <Heading color={committedCapacityColor}>{committedCapacity}</Heading>
          <Text
            marginTop="16px"
            marginRight="32px"
            marginLeft="4px"
            color={committedCapacityColor}
            fontWeight="bold"
          >
            committed
          </Text>
        </>
      )}
    </Flex>
  );
};

TitleBar.defaultProps = {
  capacity: 0,
  requestedCapacity: 0,
  committedCapacity: 0,
  teamName: 'API',
  isKanbanMode: false,
};

TitleBar.propTypes = {
  capacity: PropTypes.number,
  requestedCapacity: PropTypes.number,
  committedCapacity: PropTypes.number,
  teamName: PropTypes.string,
  isKanbanMode: PropTypes.bool,
};

export default TitleBar;
