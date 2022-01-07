import PropTypes from 'prop-types';
import React from 'react';
import { Droppable } from 'react-beautiful-dnd';
import KanbanCard from './kanbanCard';
import { ListContainer, StyledCardLabelContainer, StyledCardLabel, StyledList } from './styled';

const KanbanSwimlane = ({ kanbanSwimline, kanbanBoard }) => (
  <Droppable droppableId={kanbanSwimline.control_value__c} key={kanbanSwimline.control_value__c}>
    {(provided) => (
      <ListContainer>
        <StyledList
          ref={provided.innerRef}
          {...provided.droppableProps}
          style={{
            backgroundColor: 'gray',
          }}
        >
          <StyledCardLabelContainer
            className="swimlaneHeader"
            style={{
              backgroundColor: kanbanSwimline.accent_color_name__c,
            }}
          >
            <StyledCardLabel>{kanbanSwimline.label__c}</StyledCardLabel>
          </StyledCardLabelContainer>
          {kanbanSwimline.cards &&
            kanbanSwimline.cards.map((kanbanCard, index) => (
              <KanbanCard
                kanbanCard={kanbanCard}
                index={index}
                key={kanbanCard.id}
                swimlaneName={kanbanSwimline.control_value__c}
                kanbanBoard={kanbanBoard}
              />
            ))}
          {provided.placeholder}
        </StyledList>
      </ListContainer>
    )}
  </Droppable>
);

KanbanSwimlane.defaultProps = {
  kanbanBoard: {},
  kanbanSwimline: {},
};

KanbanSwimlane.propTypes = {
  kanbanBoard: PropTypes.shape({
    label__c: PropTypes.string,
  }),
  kanbanSwimline: PropTypes.shape({
    accent_color_name__c: PropTypes.string,
    control_value__c: PropTypes.string,
    cards: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.string,
        name__v: PropTypes.string,
      })
    ),
    label__c: PropTypes.string,
    visible__c: PropTypes.bool,
  }),
};

export default KanbanSwimlane;
