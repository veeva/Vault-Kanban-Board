import * as React from 'react';
import ReactDOM from 'react-dom';
import { ChakraProvider } from '@chakra-ui/react';

import './index.css';
import Handler from './handler';

ReactDOM.render(
  <React.StrictMode>
    <ChakraProvider>
      <Handler />
    </ChakraProvider>
  </React.StrictMode>,
  // eslint-disable-next-line no-undef
  document.getElementById('root')
);
