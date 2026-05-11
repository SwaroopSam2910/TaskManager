import { Droppable, Draggable } from "@hello-pangea/dnd";
import TaskCard from "../TaskCard/TaskCard";

function Column({ title, status, tasks, role, deleteTask, onTaskClick }) {
  return (
    <div className="column">
      <h3>{title}</h3>

      <Droppable droppableId={status}>
        {(provided) => (
          <div
            ref={provided.innerRef}
            {...provided.droppableProps}
          >

            {(tasks || []).map((task, index) => (
              <Draggable
                key={task.id}
                draggableId={String(task.id)}   // 🔥 IMPORTANT
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