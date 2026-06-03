import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/api";
import "../App.css";

export default function RegisterPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    username: "",
    password: "",
    firstName: "",
    lastName: "",
    middleName: "",
    phone: "",
    birthDate: "",
  });

  const register = async () => {
      if (
        !form.username ||
        !form.password ||
        !form.firstName ||
        !form.lastName
      ) {
        alert("Заполните обязательные поля");
        return;
      }

    try {
      await api.post("/auth/register", {
        username: form.username,
        password: form.password,
        firstName: form.firstName,
        lastName: form.lastName,
        middleName: form.middleName,
        phone: form.phone,
        birthDate: form.birthDate,
      });

      alert("Регистрация успешна");

      navigate("/login");
    } catch (error) {
      console.error(error);
      alert("Ошибка регистрации");
    }

  };

  return (
    <div className="auth-page">
      <div className="auth-card">

        <div className="auth-logo">
          BANK API
        </div>

        <div className="auth-subtitle">
          Create your account
        </div>

        <div className="auth-form">

          <input
            placeholder="Username"
            onChange={(e) =>
              setForm({ ...form, username: e.target.value })
            }
          />

          <input
            type="password"
            placeholder="Password"
            onChange={(e) =>
              setForm({ ...form, password: e.target.value })
            }
          />

          <input
            placeholder="First name"
            onChange={(e) =>
              setForm({ ...form, firstName: e.target.value })
            }
          />

          <input
            placeholder="Last name"
            onChange={(e) =>
              setForm({ ...form, lastName: e.target.value })
            }
          />

          <input
            placeholder="Middle name"
            onChange={(e) =>
              setForm({ ...form, middleName: e.target.value })
            }
          />

          <input
            placeholder="Phone"
            onChange={(e) =>
              setForm({ ...form, phone: e.target.value })
            }
          />

          <input
            type="date"
            onChange={(e) =>
              setForm({ ...form, birthDate: e.target.value })
            }
          />

          <button
            className="primary-btn"
            onClick={register}
          >
            Create Account
          </button>

        </div>

        <div className="auth-footer">
          <Link to="/login">
            Back to Login
          </Link>
        </div>

      </div>
    </div>
  );
}