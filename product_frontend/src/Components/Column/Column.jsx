import { Droppable, Draggable } from "@hello-pangea/dnd";
import TaskCard from "../TaskCard/TaskCard";
import "./Column.css";

function Column({ title, status, tasks, role, deleteTask, onTaskClick }) {
  return (
    <div className="column">
      <div className="column-header">
        <h3 className="column-title">{title}</h3>
        <span className="column-count">{(tasks || []).length}</span>
      </div>

      <Droppable droppableId={status}>
        {(provided) => (
          <div
            className="task-list"
            ref={provided.innerRef}
            {...provided.droppableProps}
          >

            {(tasks || []).map((task, index) => (
              <Draggable
                key={task.id}
                draggableId={String(task.id)}
                index={index}
              >
                {(provided) => (
                  <div
                    ref={provided.innerRef}
                    {...provided.draggableProps}
                    {...provided.dragHandleProps}
                  >

                    <TaskCard
                      task={task}
                      onClick={() => onTaskClick(task)}
                      onDelete={
                        role === "ADMIN" ? deleteTask : null
                      }
                    />

                  </div>
                )}
              </Draggable>
            ))}

            {provided.placeholder}

          </div>
        )}
      </Droppable>
    </div>
  );
}

export default Column;