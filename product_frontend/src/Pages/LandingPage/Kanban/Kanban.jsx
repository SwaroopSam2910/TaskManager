import { useEffect, useState, useCallback } from "react";
import { DragDropContext } from "@hello-pangea/dnd";
import API from "../../../services/api";
import Column from "../../../Components/Column/Column";
import "./Kanban.css";

function Kanban({ boardId, role }) {
  const [tasks, setTasks] = useState({
    TODO: [],
    PROGRESS: [],
    DONE: [],
  });

  const [members, setMembers] = useState([]);

  const [showModal, setShowModal] = useState(false);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [assignedToId, setAssignedToId] = useState("");
  const [priority, setPriority] = useState("MEDIUM");

  const [showMembers, setShowMembers] = useState(false);
  const [originalMembers, setOriginalMembers] = useState([]);
  const [hasChanges, setHasChanges] = useState(false);

  const [selectedTask, setSelectedTask] = useState(null);
  const [showTaskModal, setShowTaskModal] = useState(false);

  // 🔥 Fetch tasks
  const fetchTasks = useCallback(async () => {
    try {
      const res = await API.get(`/task/board/${boardId}`);

      setTasks({
        TODO: res.data.TODO || [],
        PROGRESS: res.data.PROGRESS || [],
        DONE: res.data.DONE || [],
      });
    } catch (err) {
      console.error("Error fetching tasks", err);
    }
  }, [boardId]);

  // 🔥 Fetch members
  const fetchMembers = useCallback(async () => {
    try {
      const res = await API.get(`/boards/${boardId}/members`);

      setMembers(res.data || []);
      setOriginalMembers(res.data || []);
      setHasChanges(false);
    } catch (err) {
      console.error("Error fetching members", err);
    }
  }, [boardId]);

  useEffect(() => {
    if (boardId) {
      fetchTasks();
      fetchMembers();
    }
  }, [fetchTasks, fetchMembers, boardId]);

  // 🔥 Role change
  const handleRoleChange = (userId, newRole) => {
    const updated = members.map((m) =>
      m.userId === userId ? { ...m, role: newRole } : m,
    );

    setMembers(updated);

    const changed = updated.some((m) => {
      const original = originalMembers.find((o) => o.userId === m.userId);
      return original && original.role !== m.role;
    });

    setHasChanges(changed);
  };

  // 🔥 Save roles
  const handleSave = async () => {
    try {
      const updates = members.filter((m) => {
        const original = originalMembers.find((o) => o.userId === m.userId);
        return original && original.role !== m.role;
      });

      for (const m of updates) {
        await API.put(`/boards/${boardId}/members/${m.userId}`, {
          role: m.role,
        });
      }

      fetchMembers();
    } catch (err) {
      console.error("Save failed", err);
    }
  };

  // 🔥 Create task
  const handleCreateTask = async () => {
    if (!title.trim()) return alert("Title required");

    try {
      await API.post("/task", {
        title,
        description,
        boardId,
        assignedToId, // ✅ now correct
        priority,
      });

      setShowModal(false);
      setTitle("");
      setDescription("");
      setAssignedToId("");
      setPriority("MEDIUM");

      fetchTasks();
    } catch (err) {
      console.error("Create failed", err);
    }
  };

  // 🔥 Delete task
  const deleteTask = async (taskId) => {
    if (!taskId) return console.error("Task ID missing");

    try {
      await API.delete(`/task/${taskId}`);
      fetchTasks();
    } catch (err) {
      console.error("Delete failed", err);
    }
  };

  // 🔥 Remove member
  const removeMember = async (userId) => {
    try {
      await API.delete(`/boards/${boardId}/members/${userId}`);
      fetchMembers();
    } catch (err) {
      console.error("Remove failed", err);
    }
  };

  // 🔥 Drag
  const handleDragEnd = async (result) => {
    if (!result.destination) return;

    const newStatus = result.destination.droppableId;
    const taskId = result.draggableId;

    try {
      await API.patch(`/task/${taskId}/status?status=${newStatus}`);
      fetchTasks();
    } catch (err) {
      console.error("Drag failed", err);
    }
  };

  return (
    <div className="kanban-container">
      {/* HEADER */}
      <div className="kanban-header">
        <h2>Kanban Board</h2>

        {role === "ADMIN" && (
          <>
            <button onClick={() => setShowModal(true)}>+ Create Task</button>
            <button onClick={() => setShowMembers(true)}>Members</button>
          </>
        )}
      </div>

      {/* COLUMNS */}
      <DragDropContext onDragEnd={handleDragEnd}>
        <div className="kanban-columns">
          <Column
            title="To Do"
            status="TODO"
            tasks={tasks.TODO}
            role={role}
            deleteTask={deleteTask}
            onTaskClick={(task) => {
              setSelectedTask(task);
              setShowTaskModal(true);
            }}
          />

          <Column
            title="In Progress"
            status="PROGRESS"
            tasks={tasks.PROGRESS}
            role={role}
            deleteTask={deleteTask}
            onTaskClick={(task) => {
              setSelectedTask(task);
              setShowTaskModal(true);
            }}
          />

          <Column
            title="Done"
            status="DONE"
            tasks={tasks.DONE}
            role={role}
            deleteTask={deleteTask}
            onTaskClick={(task) => {
              setSelectedTask(task);
              setShowTaskModal(true);
            }}
          />
        </div>
      </DragDropContext>

      {/* CREATE TASK MODAL */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-box">
            <h3>Create Task</h3>

            <input
              placeholder="Title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />

            <textarea
              placeholder="Description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />

            {/* Assign */}
            <select
              value={assignedToId}
              onChange={(e) => setAssignedToId(e.target.value)}
            >
              <option value="">Select user</option>
              {members.map((m) => (
                <option key={m.userId} value={m.userId}>
                  {m.email} ({m.role})
                </option>
              ))}
            </select>

            {/* Priority */}
            <select
              value={priority}
              onChange={(e) => setPriority(e.target.value)}
            >
              <option value="HIGH">High</option>
              <option value="MEDIUM">Medium</option>
              <option value="LOW">Low</option>
            </select>

            <div className="modal-actions">
              <button onClick={handleCreateTask}>Create</button>
              <button onClick={() => setShowModal(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}

      {/* MEMBERS MODAL */}
      {showMembers && (
        <div className="modal-overlay">
          <div className="modal-box">
            <h3>Board Members</h3>

            {members.length === 0 ? (
              <p>No members found</p>
            ) : (
              members.map((m) => (
                <div key={m.userId} className="member-row">
                  <p>{m.email}</p>

                  <select
                    value={m.role}
                    onChange={(e) => handleRoleChange(m.userId, e.target.value)}
                  >
                    <option value="ADMIN">ADMIN</option>
                    <option value="MEMBER">MEMBER</option>
                    <option value="VIEWER">VIEWER</option>
                  </select>

                  <button onClick={() => removeMember(m.userId)}>Remove</button>
                </div>
              ))
            )}

            <div className="modal-actions">
              <button disabled={!hasChanges} onClick={handleSave}>
                Save Changes
              </button>
              <button onClick={() => setShowMembers(false)}>Close</button>
            </div>
          </div>
        </div>
      )}

      {/* TASK DETAILS MODAL */}
      {showTaskModal &&
        selectedTask &&
        (() => {
          const createdUser = members.find(
            (m) => m.userId === selectedTask.createdBy,
          );

          return (
            <div className="modal-overlay">
              <div className="modal-box">
                <h3>Task Details</h3>

                <p>
                  <strong>Title:</strong> {selectedTask.title}
                </p>
                <p>
                  <strong>Description:</strong> {selectedTask.description}
                </p>

                <p>
                  <strong>Status:</strong> {selectedTask.status}
                </p>
                <p>
                  <strong>Priority:</strong> {selectedTask.priority}
                </p>

                <p>
                  <strong>Assigned To:</strong>{" "}
                  {members.find((m) => m.userId === selectedTask.assignedToId)
                    ?.email || "Unassigned"}
                </p>

                <p>
                  <strong>Created By:</strong> {createdUser?.email || "Unknown"}
                </p>

                <p>
                  <strong>Created At:</strong>{" "}
                  {new Date(selectedTask.createdAt).toLocaleString()}
                </p>

                <div className="modal-actions">
                  <button onClick={() => setShowTaskModal(false)}>Close</button>
                </div>
              </div>
            </div>
          );
        })()}
    </div>
  );
}

export default Kanban;
