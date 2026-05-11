import "./Sidebar.css";

function Sidebar({ boards, selectedBoard, setSelectedBoard }) {
  return (
    <div className="sidebar">

      <h3 className="sidebar-title">Boards</h3>

      {boards.length === 0 ? (
        <p className="empty-text">No boards available</p>
      ) : (
        <div className="board-list">
          {boards.map((board) => (
            <div
              key={board.id}
              className={`board-item ${
                selectedBoard?.id === board.id ? "active" : ""
              }`}
              onClick={() => setSelectedBoard(board)}
            >
              {board.name}
            </div>
          ))}
        </div>
      )}

    </div>
  );
}

export default Sidebar;