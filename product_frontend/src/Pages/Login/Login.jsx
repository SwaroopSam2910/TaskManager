import { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../../services/api";
import "./Login.css";

function Login() {
  const navigate = useNavigate();

  const [mode, setMode] = useState("login"); // login | register
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleLogin = async () => {
    setError("");
    setSuccess("");

    try {
      const res = await API.post("/auth/login", {
        email,
        password,
      });

      console.log("FULL RESPONSE:", res.data);
      
      localStorage.setItem("token", res.data.token);
      localStorage.setItem("role", res.data.role);

      const role = localStorage.getItem("role");
      console.log("ROLE:", role);

      window.location.href="/";

    } catch (err) {
      const msg = err.response?.data;

      if (msg === "User not found") {
        setError("User not found. Please register.");
      } else if (msg === "User not approved yet") {
        setError("Waiting for admin approval.");
      } else {
        setError("Invalid credentials.");
      }
    }
  };

  const handleRegister = async () => {
    setError("");
    setSuccess("");

    try {
      await API.post("/auth/register", {
        email,
        password,
      });

      setSuccess("Request sent. Waiting for admin approval.");
      setMode("login");

    } catch (err) {
      setError("Registration failed.");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">

        <h2 className="login-title">
          {mode === "login" ? "Sign in" : "Create account"}
        </h2>

        <p className="login-subtitle">
          {mode === "login"
            ? "Enter your credentials"
            : "Request access to the system"}
        </p>

        {/* Error */}
        {error && <p className="error-text">{error}</p>}

        {/* Success */}
        {success && <p className="success-text">{success}</p>}

        <input
          className="login-input"
          type="email"
          placeholder="Email"
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          className="login-input"
          type="password"
          placeholder="Password"
          onChange={(e) => setPassword(e.target.value)}
        />

        {/* Main Button */}
        {mode === "login" ? (
          <button className="login-button" onClick={handleLogin}>
            Login
          </button>
        ) : (
          <button className="login-button" onClick={handleRegister}>
            Register
          </button>
        )}

        {/* Toggle */}
        {mode === "login" ? (
          <p className="switch-text">
            New user?{" "}
            <span onClick={() => setMode("register")}>Register</span>
          </p>
        ) : (
          <p className="switch-text">
            Already have access?{" "}
            <span onClick={() => setMode("login")}>Login</span>
          </p>
        )}
      </div>
    </div>
  );
}

export default Login;