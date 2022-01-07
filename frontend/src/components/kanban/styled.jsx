import styled from '@emotion/styled';
import { Flex } from '@chakra-ui/react';

export const StyledLabel = styled.span`
  border-radius: 4px;
  color: #fff;
  display: block;
  overflow: hidden;
  position: relative;
  text-overflow: ellipsis;
  white-space: nowrap;
  float: left;
  height: 8px;
  margin: 0 4px 4px 0;
  max-width: 40px;
  min-width: 40px;
  padding: 0;
  text-shadow: none;
  width: auto;
`;
export const StyledCardInfo = styled.div`
  user-select: none;
  color: rgb(9, 30, 66);
  cursor: grab;
  margin: 0;
  padding: 0;
  flex-grow: 1;
  flex-basis: 100%;
  display: flex;
  flex-direction: column;
`;

export const StyledCardFeatureInfoLine = styled.div`
  user-select: none;
  color: rgb(9, 30, 66);
  cursor: grab;
  padding: 0;
  display: flex;
  margin-top: 8px;
  align-items: center;
  font-size: 12px;
  flex-wrap: wrap;
`;

export const StyledCardLabel = styled.h4`
  color: #172b4d;
  font-weight: 600;
  padding: 8px;
  transition: background-color 0.2s ease 0s;
  flex-grow: 1;
  user-select: none;
  position: relative;
  touch-action: manipulation;
  cursor: grab;
  margin-top: 0;
`;

export const StyledCardLabelContainer = styled.div`
  color: rgb(9, 30, 66);
  margin: 0;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-top-left-radius: 2px;
  border-top-right-radius: 2px;
  background-color: rgb(235, 236, 240);
  transition: background-color 0.2s ease 0s;
`;

export const StyledCard = styled(Flex)`
  text-decoration: none;
  border-radius: 2px;
  border: 2px solid transparent;
  background-color: rgb(255, 255, 255);
  box-shadow: none;
  box-sizing: border-box;
  padding: 8px;
  min-height: 40px;
  margin-bottom: 8px;
  user-select: none;
  color: rgb(9, 30, 66);
  display: flex;
  touch-action: manipulation;
  cursor: grab;
`;

export const StyledList = styled(Flex)`
  color: rgb(9, 30, 66);
  margin: 0;
  background-color: rgb(235, 236, 240);
  display: flex;
  flex-direction: column;
  opacity: inherit;
  padding: 8px 8px 0px;
  border: 8px;
  transition: background-color 0.2s ease 0s, opacity 0.1s ease 0s;
  user-select: none;
  width: 320px;
  min-height: 400px;
  overflow-anchor: none;
  border-radius: 4px;
`;

export const ListContainer = styled(Flex)`
  color: rgb(9, 30, 66);
  padding: 0;
  margin: 8px;
  display: flex;
  flex-direction: column;
`;

export const StyledBoard = styled(Flex)`
  color: rgb(9, 30, 66);
  margin: 0;
  padding: 10;
  min-height: 100vh;
  min-width: 100vw;
  display: inline-flex;
  justify-content: left;
  overflow-anchor: none;
  border-width: 1px;
  background-color: white;
  padding-top: 20px;
  padding-left: 20px;
`;
