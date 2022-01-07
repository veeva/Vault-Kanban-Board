import React from 'react';
import { Heading, Flex, Spinner } from '@chakra-ui/react';

const LoadingSpinner = () => (
  <Flex alignItems="center" justifyContent="center" height="100vh">
    <>
      <Spinner size="xl" />
      <Heading as="h4" size="sm" ml="16px">
        Loading Data
      </Heading>
    </>
  </Flex>
);

export default LoadingSpinner;
