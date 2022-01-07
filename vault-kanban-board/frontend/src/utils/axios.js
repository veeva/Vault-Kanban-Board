import axios from 'axios';

const { REACT_APP_LAMBDA_URL } = process.env;

export const post = (bodyParams) => axios.post(`${REACT_APP_LAMBDA_URL}`, bodyParams);

export const getStates = (sessionId, vaultDNS) =>
  post({
    vaultDNS,
    sessionId,
    action: 'GET_ACCESS_REQUEST_STATES',
  });

export const getKanbanData = (vaultDNS, sessionId, board, page) =>
  post({
    vaultDNS,
    sessionId,
    board,
    page,
    action: 'GET_KANBAN_DATA',
  });

export const setKanbanData = (vaultDNS, sessionId, board, page, data) =>
  post({
    vaultDNS,
    sessionId,
    board,
    page,
    data,
    action: 'SET_KANBAN_DATA',
  });
