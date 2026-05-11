import { useEffect, useState } from "react";
import API from "../../services/api";
import "./RequestList.css";

function RequestList() {
  const [requests, setRequests] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [boards, setBoards] = useState([]);
  const [boardRoles, setBoardRoles] = useState({});

  // 🔥 Fetch pending users
  const fetchRequests = async () => {
    try {
      const res = await API.get("/admin/users?status=PENDING");
      setRequests(res.data);
    } catch (err) {
      console.error("Error fetching requests", err);
    }
  };

  // 🔥 Fetch boards (will be empty for now)
  const fetchBoards = async () => {
    try {
      const res = await API.get("/boards");
      setBoards(res.data);
    } catch (err) {
      console.error("Error fetching boards", err);
    }
  };

  useEffect(() => {
    fetchRequests();
    fetchBoards();
  }, []);

  // 🔥 Open modal
  const handleApproveClick = (user) => {
    setSelectedUser(user);
    setShowModal(true);
  };

  // 🔥 Reject user
  const handleReject = async (id) => {
    try {
      await API.delete(`/admin/reject/${id}`);
      setRequests((prev) => prev.filter((u) => u.id !== id));
    } catch (err) {
      console.error("Reject failed", err);
    }
  };

  // 🔥 Final approve
  const handleFinalApprove = async () => {
    try {
      const boardsPayload = Object.entries(boardRoles).map(
        ([boardId, role]) => ({
          boardId,
          role,
        })
      );

      await API.post("/admin/approve", {
        userId: selectedUser.id,
        boards: boardsPayload,
      });

      setRequests((prev) =>
        prev.filter((u) => u.id !== selectedUser.id)
      );

      setShowModal(false);
      setBoardRoles({});
    } catch (err) {
      console.error("Approve failed", err);
    }
  };

  return (
    <div className="request-container">
      <h2>Pending Requests</h2>

      {requests.length === 0 ? (
        <p className="empty-text">No pending requests</p>
      ) : (
        <div className="request-list">
          {requests.map((user) => (
            <div key={user.id} className="request-card">
              <div>
                <p className="email">{user.email}</p>
              </div>

              <div className="actions">
                <button
                  className="approve-btn"
                  onClick={() => handleApproveClick(user)}
                >
                  Approve
                </button>

                <button
                  className="reject-btn"
                  onClick={() => handleReject(user.id)}
                >
                  Reject
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* 🔥 MODAL */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-box">
            <h3>Assign Boards & Roles</h3>

            {boards.length === 0 ? (
              <p className="empty-text">No boards available</p>
            ) : (
              boards.map((board) => (
                <div key={board.id} className="board-row">
                  <input
                    type="checkbox"
                    checked={!!boardRoles[board.id]}
                    onChange={(e) => {
                      if (e.target.checked) {
                        setBoardRoles({
                          ...boardRoles,
                          [board.id]: "MEMBER",
                        });
                      } else {
                        const updated = { ...boardRoles };
                        delete updated[board.id];
                        setBoardRoles(updated);
                      }
                    }}
                  />

                  <span>{board.name}</span>

                  {boardRoles[board.id] && (
                    <select
                      value={boardRoles[board.id]}
                      onChange={(e) =>
                        setBoardRoles({
                          ...boardRoles,
                          [board.id]: e.target.value,
                        })
                      }
                    >
                      <option value="ADMIN">ADMIN</option>
                      <option value="MEMBER">MEMBER</option>
                      <option value="VIEWER">VIEWER</option>
                    </select>
                  )}
                </div>
              ))
            )}

            <div className="modal-actions">
              <button
                className="confirm-btn"
                onClick={handleFinalApprove}
              >
                Confirm
              </button>

              <button
                className="cancel-btn"
                onClick={() => setShowModal(false)}
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default RequestList;