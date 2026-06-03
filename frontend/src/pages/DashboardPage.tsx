import { useEffect, useState } from "react";
import api from "../api/api";
import { useNavigate } from "react-router-dom";
import "../App.css";

export default function DashboardPage() {
  const navigate = useNavigate();

  const [accounts, setAccounts] = useState<any[]>([]);

  const [showCreateModal, setShowCreateModal] = useState(false);
  const [newAccountName, setNewAccountName] = useState("");

  const [showDepositModal, setShowDepositModal] = useState(false);
  const [depositAccountId, setDepositAccountId] =
    useState<number | null>(null);
  const [depositAmount, setDepositAmount] =
    useState("");

  const [showTransferModal, setShowTransferModal] = useState(false);

  const [transferForm, setTransferForm] = useState({
    fromId: "",
    toId: "",
    amount: "",
  });

const [showPhoneTransferModal, setShowPhoneTransferModal] =
  useState(false);

const [phoneTransferForm, setPhoneTransferForm] =
  useState({
    phone: "",
    amount: "",
  });

  const logout = () => {
    localStorage.removeItem("accessToken");
    navigate("/login");
  };

  const loadAccounts = async () => {
    try {
      const response = await api.get("/accounts");
      setAccounts(response.data);
    } catch (e) {
      console.error(e);
    }
  };

  const createAccount = async () => {
    if (!newAccountName.trim()) return;

    try {
      await api.post("/accounts", {
        name: newAccountName,
      });

      setNewAccountName("");
      setShowCreateModal(false);

      loadAccounts();
    } catch (e) {
      console.error(e);
      alert("Ошибка создания счёта");
    }
  };

  const deposit = async () => {
    if (!depositAccountId) return;

    try {
      await api.post("/accounts/deposit", {
        accountId: depositAccountId,
        amount: Number(depositAmount),
      });

      setDepositAmount("");
      setDepositAccountId(null);
      setShowDepositModal(false);

      loadAccounts();
    } catch (e) {
      console.error(e);
      alert("Ошибка пополнения");
    }
  };

const transferBetweenAccounts = async () => {
  try {
    await api.post(
      `/accounts/transfer?fromId=${transferForm.fromId}&toId=${transferForm.toId}&amount=${transferForm.amount}`
    );

    setTransferForm({
      fromId: "",
      toId: "",
      amount: "",
    });

    setShowTransferModal(false);

    loadAccounts();
  } catch (e) {
    console.error(e);
    alert("Ошибка перевода");
  }
};

const transferByPhone = async () => {
  try {

    if (
      !phoneTransferForm.phone ||
      !phoneTransferForm.amount
    ) {
      alert("Заполните все поля");
      return;
    }

    await api.post(
      "/accounts/transfer/phone",
      {
        phone: phoneTransferForm.phone,
        amount: Number(
          phoneTransferForm.amount
        ),
        categoryId: 1,
      }
    );

    setPhoneTransferForm({
      phone: "",
      amount: "",
    });

    setShowPhoneTransferModal(false);

    loadAccounts();

  } catch (e) {
    console.error(e);
    alert("Ошибка перевода");
  }
};

  useEffect(() => {
    loadAccounts();
  }, []);

  const totalBalance = accounts.reduce(
    (sum, account) => sum + Number(account.balance),
    0
  );

  return (
    <div className="dashboard">

      <div className="dashboard-header">

        <div>
          <h1>Welcome back 👋</h1>
          <p>Manage your accounts and transfers</p>
        </div>

        <button
          className="logout-btn"
          onClick={logout}
        >
          Logout
        </button>

      </div>

      <div className="balance-card">

        <div className="balance-title">
          Total Balance
        </div>

        <div className="balance-amount">
          {totalBalance.toLocaleString()} ₽
        </div>

      </div>

      <div className="dashboard-actions">

        <button
          className="action-btn"
          onClick={() => setShowCreateModal(true)}
        >
          Create Account
        </button>

        <button
          className="action-btn"
          onClick={() => setShowTransferModal(true)}
        >
          Transfer
        </button>

        <button
          className="action-btn"
          onClick={() =>
            setShowPhoneTransferModal(true)
          }
        >
          Transfer by Phone
        </button>

      </div>

      <h2 style={{ marginBottom: "20px" }}>
        My Accounts
      </h2>

      <div className="accounts-grid">

        {accounts.map((account: any) => (

          <div
            key={account.id}
            className="account-card"
          >

            <div className="account-name">
              💳 {account.name}
            </div>

            <div className="account-balance">
              {account.balance} ₽
            </div>

            <div className="account-meta">
              ID #{account.id}
            </div>

            <div className="account-meta">
              {account.primary
                ? "Primary Account"
                : "Additional Account"}
            </div>

            <div className="account-actions">

              <button
                className="account-btn"
                onClick={() => {
                  setDepositAccountId(account.id);
                  setShowDepositModal(true);
                }}
              >
                Deposit
              </button>

            </div>

          </div>

        ))}

      </div>

      {showCreateModal && (
        <div className="modal-overlay">
          <div className="modal">

            <div className="modal-title">
              Create Account
            </div>

            <input
              placeholder="Account name"
              value={newAccountName}
              onChange={(e) =>
                setNewAccountName(e.target.value)
              }
            />

            <div className="modal-actions">

              <button
                className="modal-btn cancel-btn"
                onClick={() =>
                  setShowCreateModal(false)
                }
              >
                Cancel
              </button>

              <button
                className="modal-btn"
                onClick={createAccount}
              >
                Create
              </button>

            </div>

          </div>
        </div>
      )}

      {showDepositModal && (
        <div className="modal-overlay">
          <div className="modal">

            <div className="modal-title">
              Deposit
            </div>

            <input
              placeholder="Amount"
              value={depositAmount}
              onChange={(e) =>
                setDepositAmount(e.target.value)
              }
            />

            <div className="modal-actions">

              <button
                className="modal-btn cancel-btn"
                onClick={() =>
                  setShowDepositModal(false)
                }
              >
                Cancel
              </button>

              <button
                className="modal-btn"
                onClick={deposit}
              >
                Confirm
              </button>

            </div>

          </div>
        </div>
      )}

  {showTransferModal && (
    <div className="modal-overlay">

      <div className="modal">

        <div className="modal-title">
          Transfer Between Accounts
        </div>

        <input
          placeholder="From Account ID"
          value={transferForm.fromId}
          onChange={(e) =>
            setTransferForm({
              ...transferForm,
              fromId: e.target.value,
            })
          }
        />

        <br />
        <br />

        <input
          placeholder="To Account ID"
          value={transferForm.toId}
          onChange={(e) =>
            setTransferForm({
              ...transferForm,
              toId: e.target.value,
            })
          }
        />

        <br />
        <br />

        <input
          placeholder="Amount"
          value={transferForm.amount}
          onChange={(e) =>
            setTransferForm({
              ...transferForm,
              amount: e.target.value,
            })
          }
        />

        <div className="modal-actions">

          <button
            className="modal-btn cancel-btn"
            onClick={() =>
              setShowTransferModal(false)
            }
          >
            Cancel
          </button>

          <button
            className="modal-btn"
            onClick={transferBetweenAccounts}
          >
            Transfer
          </button>

        </div>

      </div>

    </div>
  )}

  {showPhoneTransferModal && (
    <div className="modal-overlay">

      <div className="modal">

        <div className="modal-title">
          Transfer by Phone
        </div>

        <input
          placeholder="+79991234567"
          value={phoneTransferForm.phone}
          onChange={(e) =>
            setPhoneTransferForm({
              ...phoneTransferForm,
              phone: e.target.value,
            })
          }
        />

        <br />
        <br />

        <input
          placeholder="Amount"
          value={phoneTransferForm.amount}
          onChange={(e) =>
            setPhoneTransferForm({
              ...phoneTransferForm,
              amount: e.target.value,
            })
          }
        />

        <div className="modal-actions">

          <button
            className="modal-btn cancel-btn"
            onClick={() =>
              setShowPhoneTransferModal(false)
            }
          >
            Cancel
          </button>

          <button
            className="modal-btn"
            onClick={transferByPhone}
          >
            Transfer
          </button>

        </div>

      </div>

    </div>
  )}

    </div>
  );
}
