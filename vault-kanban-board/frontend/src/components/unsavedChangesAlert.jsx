import React, { useState } from 'react';
import PropTypes from 'prop-types';
import {
  AlertDialog,
  AlertDialogBody,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogOverlay,
  AlertDialogFooter,
  Button,
} from '@chakra-ui/react';

const UnSavedChangesAlert = ({ cancelRef, onClose, isOpen, discardFeatures }) => {
  const [isDiscarding, setDiscarding] = useState(false);
  return (
    <AlertDialog isOpen={isOpen} leastDestructiveRef={cancelRef} onClose={onClose}>
      <AlertDialogOverlay>
        <AlertDialogContent>
          <AlertDialogHeader fontSize="lg" fontWeight="bold">
            Unsaved Changes
          </AlertDialogHeader>

          <AlertDialogBody>You have unsaved changes!</AlertDialogBody>

          <AlertDialogFooter>
            <Button
              colorScheme="blue"
              isLoading={isDiscarding}
              loadingText="Loading"
              spinnerPosition="start"
              onClick={() => {
                setDiscarding(true);
                discardFeatures();
              }}
              mr={3}
            >
              Discard Changes
            </Button>
            <Button ref={cancelRef} onClick={onClose}>
              Close
            </Button>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialogOverlay>
    </AlertDialog>
  );
};

UnSavedChangesAlert.defaultProps = {
  cancelRef: {},
  onClose: () => {},
  isOpen: true,
  discardFeatures: () => {},
};

UnSavedChangesAlert.propTypes = {
  cancelRef: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.shape({ current: PropTypes.instanceOf(Element) }),
  ]),
  onClose: PropTypes.func,
  isOpen: PropTypes.bool,
  discardFeatures: PropTypes.func,
};

export default UnSavedChangesAlert;
