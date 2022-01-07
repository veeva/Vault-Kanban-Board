import PropTypes from 'prop-types';
import React from 'react';
import { Draggable } from 'react-beautiful-dnd';
import { Box } from '@chakra-ui/react';
import { StyledCard, StyledCardInfo } from './styled';

const KanbanCard = ({ kanbanCard, index, kanbanBoard }) => (
  <Draggable draggableId={kanbanCard.id} index={index} key={kanbanCard.id}>
    {(provider) => (
      <StyledCard
        key={kanbanCard.id}
        index={index}
        {...provider.draggableProps}
        {...provider.dragHandleProps}
        ref={provider.innerRef}
        fontSize={['12px', '14px']}
        style={{
          ...provider.draggableProps.style,
          position: 'static',
          backgroundColor: 'White',
        }}
      >
        <StyledCardInfo>
          <Box display="inline-flex" className="cardTitle">
            {kanbanCard.name__v}
          </Box>
          <Box>
            <table>
              {kanbanBoard &&
                kanbanBoard.card_fields
                  // eslint-disable-next-line react/prop-types
                  .filter((kanbanSwimline) => kanbanSwimline.visible__c)
                  .map((kanbanCardField) => (
                    <tr>
                      <td className="cardFieldLabel">{kanbanCardField.label__c}</td>
                      <td className="cardFieldValue">
                        {kanbanCard.old_values[kanbanCardField.field_api_name__c]}
                      </td>
                    </tr>
                  ))}
            </table>
          </Box>
        </StyledCardInfo>
      </StyledCard>
    )}
  </Draggable>
);

KanbanCard.defaultProps = {
  kanbanBoard: {},
  kanbanCard: {},
  index: 0,
};

KanbanCard.propTypes = {
  kanbanBoard: PropTypes.shape({
    label__c: PropTypes.string,
    card_fields: PropTypes.shape({
      field_api_name__c: PropTypes.string,
      id: PropTypes.string,
      label__c: PropTypes.string,
      name__v: PropTypes.string,
      visible__c: PropTypes.bool,
    }),
  }),
  kanbanCard: PropTypes.shape({
    id: PropTypes.string,
    label__c: PropTypes.string,
    name__v: PropTypes.string,
    old_values: PropTypes.shape({}),
  }),
  index: PropTypes.number,
};

export default KanbanCard;
