/* eslint-disable no-unused-vars */
import * as lodashGet from 'lodash.get';
import React, { useEffect, useState } from 'react';
import { Container } from '@chakra-ui/react';
import PropTypes from 'prop-types';
import LoadingSpinner from './components/loadingSpinner';
import NotAuthenticatedAlert from './components/notAuthenticatedAlert';
import KanbanView from './components/kanban/kanbanBoard';

function App({ vaultDNS, sessionId, board, page }) {
  const [showAuthAlert, setAuthAlertState] = useState(false);
  const authAlertClose = () => setAuthAlertState(false);
  const cancelRef = React.useRef();

  return (
    <>
      {showAuthAlert && <LoadingSpinner />}
      {showAuthAlert && (
        <NotAuthenticatedAlert
          cancelRef={cancelRef}
          isOpen={showAuthAlert}
          onClose={authAlertClose}
        />
      )}
      <Container
        alignItems="left"
        justifyContent="center"
        flexDirection="column"
        marginTop="10px"
        marginLeft="32px"
        marginBottom="20px"
        width="calc(100% - 64px)"
        maxWidth="calc(100% - 64px)"
        pr="0"
        pl="0"
        mr="0"
      />

      {!showAuthAlert && (
        <KanbanView vaultDNS={vaultDNS} sessionId={sessionId} board={board} page={page} />
      )}
    </>
  );
}

App.defaultProps = {
  vaultDNS: '',
  sessionId: '',
  board: '',
  page: '',
};

App.propTypes = {
  vaultDNS: PropTypes.string,
  sessionId: PropTypes.string,
  board: PropTypes.string,
  page: PropTypes.number,
};

export default App;
