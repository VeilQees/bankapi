import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";
import "../App.css";

export default function TransactionsPage() {
  const navigate = useNavigate();

  const [transactions, setTransactions] = useState<any[]>([]);

  const loadTransactions = async () => {
    try {
      const response = await api.get("/transactions");
      setTransactions(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    loadTransactions();
  }, []);

  return (
    <div className="dashboard">

      <div className="dashboard-header">

        <div>
          <h1>Transactions History</h1>
          <p>All your operations</p>
        </div>

        <button
          className="action-btn"
          onClick={() => navigate("/dashboard")}
        >
          Back
        </button>

      </div>

      <div className="accounts-grid">

        {[...transactions]
          .sort(
            (a, b) =>
              new Date(b.createdAt).getTime() -
              new Date(a.createdAt).getTime()
          )
          .map((transaction) => (

          <div
            key={transaction.id}
            className="account-card transaction-card"
          >

            <div className="account-name">
              #{transaction.id}
            </div>

            <div className="account-meta">
              Sender: {transaction.fromUser}
            </div>

            <div className="account-meta">
              Receiver: {transaction.toUser}
            </div>

            <div className="transaction-amount">
              {transaction.amount} ₽
            </div>

            <div className="account-meta">
              Category: {transaction.category}
            </div>

            <div className="transaction-date">
              {new Date(
                transaction.createdAt
              ).toLocaleString()}
            </div>

          </div>

        ))}

      </div>

    </div>
  );
}