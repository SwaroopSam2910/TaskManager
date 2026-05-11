import "./TaskCard.css";

function TaskCard({ task, onDelete, onClick }) {
  return (
    <div className="task-card" onClick={onClick}>

      <p className="task-title">{task.title}</p>

      {/* Priority */}
      <span className={`priority ${task.priority}`}>
        {task.priority}
      </span>

      {/* Delete */}
      {onDelete && (
        <span
          className="delete-icon"
          onClick={(e) => {
            e.stopPropagation(); // 🔥 IMPORTANT
            onDelete(task.id);
          }}
        >
          🗑️
        </span>
      )}

    </div>
  );
}

export default TaskCard;