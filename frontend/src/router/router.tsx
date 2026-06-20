import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import DashboardPage from "../pages/DashboardPage";
import TransactionsPage from "../pages/TransactionsPage";

export default function AppRouter() {
  const token = localStorage.getItem("accessToken");

  return (
    <BrowserRouter>
      <Routes>

        <Route
          path="/"
          element={<Navigate to="/login" replace />}
        />

        <Route
          path="/login"
          element={<LoginPage />}
        />

        <Route
          path="/register"
          element={<RegisterPage />}
        />

        <Route
          path="/dashboard"
          element={
            token
              ? <DashboardPage />
              : <Navigate to="/login" replace />
          }
        />

        <Route
          path="/transactions"
          element={
            token
              ? <TransactionsPage />
              : <Navigate to="/login" replace />
          }
        />

      </Routes>
    </BrowserRouter>
  );
}