import React from 'react';
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

const NotAuthenticatedAlert = ({ cancelRef, onClose, isOpen }) => (
  <AlertDialog isOpen={isOpen} leastDestructiveRef={cancelRef} onClose={onClose}>
    <AlertDialogOverlay>
      <AlertDialogContent>
        <AlertDialogHeader fontSize="lg" fontWeight="bold">
          Not Authenticated
        </AlertDialogHeader>

        <AlertDialogBody>
          Log into Veeva Vault to authenticate your session and reload.
        </AlertDialogBody>

        <AlertDialogFooter>
          <Button ref={cancelRef} onClick={onClose}>
            Close
          </Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialogOverlay>
  </AlertDialog>
);

NotAuthenticatedAlert.defaultProps = {
  cancelRef: {},
  onClose: () => {},
  isOpen: true,
};

NotAuthenticatedAlert.propTypes = {
  cancelRef: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.shape({ current: PropTypes.instanceOf(Element) }),
  ]),
  onClose: PropTypes.func,
  isOpen: PropTypes.bool,
};

export default NotAuthenticatedAlert;
