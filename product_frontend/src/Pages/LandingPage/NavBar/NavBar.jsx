import { useNavigate } from "react-router-dom";
import { useState } from "react";
import API from "../../../services/api"; // adjust path if needed
import "./NavBar.css";

function Navbar() {
  const navigate = useNavigate();
  const role = localStorage.getItem("role");

  // 🔥 Modal state
  const [showModal, setShowModal] = useState(false);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");

  // 🔥 Create Board API
  const handleCreateBoard = async () => {
    if (!name.trim()) {
      alert("Board name is required");
      return;
    }

    try {
      await API.post("/boards/create", {
        name,
        description,
      });

      setShowModal(false);
      setName("");
      setDescription("");

      // optional refresh
      window.location.reload();

    } catch (err) {
      console.error("Create board failed", err);
    }
  };

  return (
    <>
      <div className="navbar">

        {/* 🔷 Left */}
        <h2 className="logo" onClick={() => navigate("/")}>
          TaskManager
        </h2>

        {/* 🔷 Right */}
        <div className="nav-links">

          {/* ✅ ADMIN: Create Board */}
          {role === "ADMIN" && (
            <button
              className="create-btn"
              onClick={() => setShowModal(true)}
            >
              + Create Board
            </button>
          )}

          {/* ✅ ADMIN: Requests */}
          {role === "ADMIN" && (
            <button onClick={() => navigate("/requests")}>
              Requests
            </button>
          )}

          <button
            className="logout-btn"
            onClick={() => {
              localStorage.clear();
              navigate("/login");
            }}
          >
            Logout
          </button>

        </div>
      </div>

      {/* 🔥 MODAL */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-box">

            <h3>Create Board</h3>

            <input
              type="text"
              placeholder="Board name"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />

            <textarea
              placeholder="Description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />

            <div className="modal-actions">
              <button onClick={handleCreateBoard}>
                Create
              </button>

              <button onClick={() => setShowModal(false)}>
                Cancel
              </button>
            </div>

          </div>
        </div>
      )}
    </>
  );
}

export default Navbar;