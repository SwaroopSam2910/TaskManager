import { useEffect, useState } from "react";
import API from "../../services/api";
import Kanban from "./Kanban/Kanban";
import Sidebar from "./Sidebar/Sidebar";
import Navbar from "./NavBar/NavBar";
import "./LandingPage.css";

function LandingPage() {

  const [boards, setBoards] = useState([]);
  const [selectedBoard, setSelectedBoard] = useState(null);

  // 🔥 Fetch boards
  const fetchBoards = async () => {
    try {
      const res = await API.get("/boards");
      setBoards(res.data);
    } catch (err) {
      console.error("Error fetching boards", err);
    }
  };

  useEffect(() => {
    fetchBoards();
  }, []);

  return (
    <div className="landing-container">

      {/* 🔷 NAVBAR */}
      <Navbar />

      {/* 🔷 BODY */}
      <div className="body-container">

        <Sidebar
        boards={boards}
        selectedBoard={selectedBoard}
        setSelectedBoard={setSelectedBoard}
        />

        {/* 🔵 MAIN CONTENT */}
        <div className="main-content">
          {selectedBoard ? (
            <Kanban
              boardId={selectedBoard.id}
              role={selectedBoard.role} // 🔥 board-level role
            />
          ) : (
            <div className="empty-state">
              <h2>Select a board</h2>
              <p>Nothing to display until a board is selected</p>
            </div>
          )}
        </div>

      </div>
    </div>
  );
}

export default LandingPage;