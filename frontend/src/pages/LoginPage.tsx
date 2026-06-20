import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/api";
import "../App.css";

export default function LoginPage() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const login = async () => {
    try {
      const response = await api.post("/auth/login", {
        username,
        password,
      });

      localStorage.setItem(
        "accessToken",
        response.data.accessToken
      );

      await api.get("/accounts");

      navigate("/dashboard");
    } catch (error) {
        console.error(error);
        alert("Неверный логин или пароль");
      }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">

        <div className="auth-logo">
          BANK API
        </div>

        <div className="auth-subtitle">
          Modern Banking Experience
        </div>

        <div className="auth-form">

          <input
            placeholder="Username"
            value={username}
            onChange={(e) =>
              setUsername(e.target.value)
            }
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) =>
              setPassword(e.target.value)
            }
          />

          <button
            className="primary-btn"
            onClick={login}
          >
            Sign In
          </button>

        </div>

        <div className="auth-footer">
          <Link to="/register">
            Создать аккаунт
          </Link>
        </div>

      </div>
    </div>
  );
}