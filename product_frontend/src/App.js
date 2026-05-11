import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./Pages/Login/Login";
import LandingPage from "./Pages/LandingPage/LandingPage";
import RequestList from "./Components/RequestList/RequestList"; // adjust path if needed

function App() {
  const token = localStorage.getItem("token");

  return (
    <BrowserRouter>
      <Routes>
        {/* Landing Page */}
        <Route
          path="/"
          element={token ? <LandingPage /> : <Navigate to="/login" />}
        />

        {/* Login */}
        <Route path="/login" element={<Login />} />

        <Route
          path="/requests"
          element={token ? <RequestList /> : <Navigate to="/login" />}
        />

      </Routes>
    </BrowserRouter>
  );
}

export default App;
