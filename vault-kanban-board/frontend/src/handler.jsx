import * as React from 'react';

import './index.css';
import App from './app';
import LoadingSpinner from './components/loadingSpinner';

class Handler extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      vaultDNS: null,
      sessionId: null,
      board: null,
      page: null,
    };
    this.receiveMessage = this.receiveMessage.bind(this);
  }

  componentDidMount() {
    const readyMessage = JSON.stringify({
      message_id: 'ready',
      data: {},
    });
    const urlParams = new URLSearchParams(window.location.search);
    this.setState({
      vaultDNS: urlParams.has('vaultDNS') ? urlParams.get('vaultDNS') : null,
      sessionId: urlParams.has('sessionId') ? urlParams.get('sessionId') : null,
      board: urlParams.has('board') ? urlParams.get('board') : null,
      page: urlParams.has('page') ? Number(urlParams.get('page')) : 1,
    });
    // eslint-disable-next-line react/destructuring-assignment
    if (!this.state.sessionId) {
      window.parent.postMessage(readyMessage, '*');
      window.addEventListener('message', this.receiveMessage);
    }
  }

  receiveMessage(event) {
    const message = JSON.parse(event.data);
    if (message.message_id === 'session_id') {
      this.setState({
        sessionId: message.data.session_id,
      });
      window.removeEventListener('message', this.receiveMessage);
    }
  }

  render() {
    // eslint-disable-next-line react/destructuring-assignment
    if (this.state.sessionId)
      return (
        <App
          sessionId={
            /* eslint-disable-next-line react/destructuring-assignment */
            this.state.sessionId
          }
          vaultDNS={
            /* eslint-disable-next-line react/destructuring-assignment */
            this.state.vaultDNS
          }
          board={
            /* eslint-disable-next-line react/destructuring-assignment */
            this.state.board
          }
          page={
            /* eslint-disable-next-line react/destructuring-assignment */
            this.state.page
          }
        />
      );
    return <LoadingSpinner />;
  }
}

export default Handler;
