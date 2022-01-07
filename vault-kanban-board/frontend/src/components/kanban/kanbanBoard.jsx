import { Box, Button, Center, Container, Flex, Heading, Spinner } from '@chakra-ui/react';
import PropTypes from 'prop-types';
import React, { useEffect, useState } from 'react';
import { DragDropContext } from 'react-beautiful-dnd';
import { ArrowLeftIcon, ArrowRightIcon, CheckIcon } from '@chakra-ui/icons';
import KanbanSwimlane from './kanbanSwimlane';
import { StyledBoard } from './styled';
import { getKanbanData, setKanbanData } from '../../utils/axios';

const KanbanView = ({ vaultDNS, sessionId, board, page }) => {
  const [spinner, setSpinner] = useState(null);
  const [kanbanBoard, setKanbanBoard] = useState(null);
  const [boardLabel, setBoardLabel] = useState('');
  const [allowSave, setAllowSave] = useState('');
  const [pageNumber, setPageNumber] = useState('');

  const getData = async (newPageNumber) => {
    console.log(`newPageNumber = ${newPageNumber}`);
    setSpinner('Loading data');
    const kanbanDataResponse = await getKanbanData(vaultDNS, sessionId, board, newPageNumber);
    console.log(kanbanDataResponse);
    setKanbanBoard(kanbanDataResponse.data.data);
    console.log(kanbanDataResponse.data.data);
    setBoardLabel(kanbanDataResponse.data.data.label__c);
    setAllowSave(kanbanDataResponse.data.data.allow_save__c);
    setSpinner(null);
    setPageNumber(newPageNumber);
  };

  const getNextPage = async () => {
    console.log(pageNumber);
    if (pageNumber < kanbanBoard.page_count__c) {
      await getData(pageNumber + 1);
    }
  };

  const getPrevPage = async () => {
    console.log(pageNumber);
    if (pageNumber > 1) {
      await getData(pageNumber - 1);
    }
  };

  const handleKeyPress = async (e) => {
    if (e.key === 'ArrowLeft') {
      // await getPrevPage();
    } else if (e.key === 'ArrowRight') {
      // await getNextPage();
    }
  };

  useEffect(() => {
    document.addEventListener('keydown', handleKeyPress);
    if (!kanbanBoard) {
      getData(page);
    }
  }, []);

  const handleFeatureMove = (destination, source, draggableId) => {
    console.log(destination);
    console.log(source);
    console.log(draggableId);

    const tempBoard = JSON.parse(JSON.stringify(kanbanBoard));

    // find the destination swimlane
    const destinationSwimlaneName = destination.droppableId;
    const destinationSwimlane = tempBoard.swimlanes.find(
      (kanbanSwimline) => kanbanSwimline.control_value__c === destinationSwimlaneName
    );

    // find the source swimlane
    const sourceSwimlaneName = source.droppableId;
    const sourceSwimlane = tempBoard.swimlanes.find(
      (kanbanSwimline) => kanbanSwimline.control_value__c === sourceSwimlaneName
    );

    const sourceCard = sourceSwimlane.cards.find((kanbanCard) => kanbanCard.id === draggableId);
    const controlField = kanbanBoard.control_field__c;

    if (!sourceCard.new_values) {
      sourceCard.new_values = { id: null, controlField: null };
    }
    sourceCard.new_values.id = draggableId;
    sourceCard.new_values[controlField] = destinationSwimlaneName;

    let destinationCards = null;
    if (destinationSwimlane.cards) {
      destinationCards = [
        ...destinationSwimlane.cards.filter(
          (kanbanCard) => kanbanCard.name__v < sourceCard.name__v
        ),
        sourceCard,
        ...destinationSwimlane.cards.filter(
          (kanbanCard) => kanbanCard.name__v > sourceCard.name__v
        ),
      ];
      destinationSwimlane.cards = destinationCards;
    } else destinationSwimlane.cards = [sourceCard];

    if (sourceSwimlane.cards && sourceSwimlane.cards.length > 1) {
      sourceSwimlane.cards = sourceSwimlane.cards
        .filter((kanbanCard) => kanbanCard.id !== draggableId)
        .map((kanbanCard) => kanbanCard);
    } else sourceSwimlane.cards = [];

    setKanbanBoard(tempBoard);
  };

  const onDragEnd = (result) => {
    if (kanbanBoard.allow_save__c) {
      const { destination, source, draggableId } = result;
      if (!destination) {
        return;
      }
      if (destination.droppableId === source.droppableId) {
        return;
      }
      handleFeatureMove(destination, source, draggableId);
    }
  };

  const updateFeatures = async () => {
    async function saveData() {
      setSpinner('Saving data');
      if (kanbanBoard) {
        const kanbanDataResponse = await setKanbanData(
          vaultDNS,
          sessionId,
          board,
          page,
          kanbanBoard
        );
        console.log(kanbanDataResponse);
        setKanbanBoard(kanbanDataResponse.data.data);
        console.log(kanbanDataResponse.data.data);
        setBoardLabel(kanbanDataResponse.data.data.label__c);
        setAllowSave(kanbanDataResponse.data.data.allow_save__c);
      }
      setSpinner(null);
    }
    saveData();
  };

  return (
    <Container
      maxH="500px"
      maxWidth="calc(100% - 32px)"
      width="calc(100% - 32px)"
      pr="0"
      pl="0"
      mr="0"
      ml="0"
    >
      <Flex className="boardToolbar">
        <Heading paddingLeft={15} className="boardLabel" fontSize="32px">
          {boardLabel}
        </Heading>
        {allowSave && allowSave === true && (
          <Box marginLeft="auto">
            <Button
              colorScheme="green"
              size="sm"
              leftIcon={<CheckIcon />}
              onClick={() => updateFeatures()}
            >
              Save
            </Button>
          </Box>
        )}
      </Flex>
      {spinner ? (
        <Center mt="16px" mb="16px">
          <Spinner size="md" />
          <Heading as="h4" size="sm" ml="16px">
            {spinner}
          </Heading>
        </Center>
      ) : (
        // eslint-disable-next-line react/style-prop-object
        <StyledBoard onKeyPress={handleKeyPress}>
          <Flex paddingTop="8px">
            <Box>
              <Button
                disabled={pageNumber <= 1}
                paddingBottom="200"
                paddingLeft="2"
                paddingRight="0"
                paddingTop="200"
                colorScheme={pageNumber > 1 ? 'blackAlpha' : 'white'}
                size="lg"
                leftIcon={pageNumber > 1 ? <ArrowLeftIcon /> : ''}
                onClick={() => getPrevPage()}
              />
            </Box>
          </Flex>
          <DragDropContext onDragEnd={onDragEnd}>
            {kanbanBoard &&
              // eslint-disable-next-line array-callback-return
              kanbanBoard.swimlanes
                .filter((kanbanSwimline) => kanbanSwimline.visible__c)
                .map((kanbanSwimline) => (
                  <KanbanSwimlane
                    key={kanbanSwimline.control_value__c}
                    kanbanSwimline={kanbanSwimline}
                    kanbanBoard={kanbanBoard}
                  />
                ))}
          </DragDropContext>
          <Flex paddingTop="8px">
            {kanbanBoard && pageNumber < kanbanBoard.page_count__c && (
              <Box>
                <Button
                  paddingBottom="200"
                  paddingLeft="2"
                  paddingRight="0"
                  paddingTop="200"
                  colorScheme="blackAlpha"
                  size="lg"
                  leftIcon={<ArrowRightIcon />}
                  onClick={() => getNextPage()}
                />
              </Box>
            )}
          </Flex>
        </StyledBoard>
      )}
    </Container>
  );
};

KanbanView.defaultProps = {
  vaultDNS: '',
  sessionId: '',
  board: '',
  page: '',
};

KanbanView.propTypes = {
  vaultDNS: PropTypes.string,
  sessionId: PropTypes.string,
  board: PropTypes.string,
  page: PropTypes.number,
};

export default KanbanView;
