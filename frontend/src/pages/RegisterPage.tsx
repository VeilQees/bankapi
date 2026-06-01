import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/api";

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
    try {
      const payload = {
        username: form.username,
        password: form.password,
        firstName: form.firstName,
        lastName: form.lastName,
        middleName: form.middleName,
        phone: form.phone,
        birthDate: form.birthDate,
      };

      console.log("REGISTER PAYLOAD:", payload);

      const response = await api.post("/auth/register", payload);

      if (response.data.accessToken) {
        localStorage.setItem(
          "accessToken",
          response.data.accessToken
        );

        navigate("/dashboard");
      } else {
        navigate("/login");
      }

      alert("Регистрация успешна");

      navigate("/login");

    } catch (error) {
      console.error(error);
      alert("Ошибка регистрации");
    }
  };

  return (
    <div style={{ padding: "40px" }}>
      <h1>Register</h1>

      <input
        placeholder="Username"
        onChange={(e) =>
          setForm({ ...form, username: e.target.value })
        }
      />

      <br /><br />

      <input
        type="password"
        placeholder="Password"
        onChange={(e) =>
          setForm({ ...form, password: e.target.value })
        }
      />

      <br /><br />

      <input
        placeholder="Имя"
        onChange={(e) =>
          setForm({ ...form, firstName: e.target.value })
        }
      />

      <br /><br />

      <input
        placeholder="Фамилия"
        onChange={(e) =>
          setForm({ ...form, lastName: e.target.value })
        }
      />

      <br /><br />

      <input
        placeholder="Отчество"
        onChange={(e) =>
          setForm({ ...form, middleName: e.target.value })
        }
      />

      <br /><br />

      <input
        placeholder="Телефон"
        onChange={(e) =>
          setForm({ ...form, phone: e.target.value })
        }
      />

      <br /><br />

      <input
        type="date"
        onChange={(e) =>
          setForm({ ...form, birthDate: e.target.value })
        }
      />

      <br /><br />

      <button onClick={register}>
        Register
      </button>

      <br /><br />

      <Link to="/login">
        Назад ко входу
      </Link>
    </div>
  );
}