import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/api";

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
    <div style={{ padding: "40px" }}>
      <h1>Login</h1>

      <input
        placeholder="Username"
        value={username}
        onChange={(e) =>
          setUsername(e.target.value)
        }
      />

      <br /><br />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) =>
          setPassword(e.target.value)
        }
      />

      <br /><br />

      <button onClick={login}>
        Login
      </button>

      <br /><br />

      <Link to="/register">
        Нет аккаунта? Зарегистрироваться
      </Link>
    </div>
  );
}