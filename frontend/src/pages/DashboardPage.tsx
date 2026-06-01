import { useEffect, useState } from "react";
import api from "../api/api";
import { useNavigate } from "react-router-dom";

export default function DashboardPage() {
  const navigate = useNavigate();

  const [accounts, setAccounts] = useState<any[]>([]);
  const [primaryAccountId, setPrimaryAccountId] = useState<number | null>(null);

  const logout = () => {
    localStorage.removeItem("accessToken");
    navigate("/login");
  };

  const loadAccounts = async () => {
    try {
      const response = await api.get("/accounts");
      setAccounts(response.data);

      const primary = response.data.find((a: any) => a.primary);

      if (primary) {
        setPrimaryAccountId(primary.id);
      }

    } catch (e) {
      console.error(e);
    }
  };

  const createAccount = async () => {
    const name = prompt("Название счёта");

    if (!name) return;

    await api.post("/accounts", {
      name,
    });

    loadAccounts();
  };

  const deposit = async (accountId: number) => {
    const amount = prompt("Сумма пополнения");

    if (!amount) return;

    await api.post("/accounts/deposit", {
      accountId,
      amount: Number(amount),
    });

    loadAccounts();
  };

  const transferBetweenAccounts = async () => {
    if (accounts.length < 2) {
      alert("Нужно минимум 2 счёта");
      return;
    }

    const fromId = prompt("ID счёта отправителя");
    const toId = prompt("ID счёта получателя");
    const amount = prompt("Сумма");

    if (!fromId || !toId || !amount) return;

    await api.post(
      `/accounts/transfer?fromId=${fromId}&toId=${toId}&amount=${amount}`
    );

    loadAccounts();
  };

 const transferByPhone = async () => {
   const phone = window.prompt("Телефон получателя");

   if (!phone) return;

   const amountInput = window.prompt("Введите сумму перевода");

   if (!amountInput) return;

   const amount = Number(amountInput);

   if (isNaN(amount)) {
     alert("Некорректная сумма");
     return;
   }

   try {
     await api.post("/accounts/transfer/phone", {
       phone,
       amount,
       categoryId: 1
     });

     await loadAccounts();

     alert("Перевод выполнен");
   } catch (e) {
     console.error(e);
     alert("Ошибка перевода");
   }
 };

  useEffect(() => {
    loadAccounts();
  }, []);

  return (
    <div style={{ padding: "30px" }}>
      <h1>Bank Dashboard</h1>

      <button onClick={createAccount}>
        Создать счёт
      </button>

      <br /><br />

      <button onClick={transferBetweenAccounts}>
        Перевод между счетами
      </button>

      <br /><br />

      <button onClick={transferByPhone}>
        Перевод по номеру телефона
      </button>

      <br /><br />

      <h2>Мои счета</h2>

      {accounts.map((account: any) => (
        <div
          key={account.id}
          style={{
            border: "1px solid gray",
            padding: "10px",
            marginBottom: "10px"
          }}
        >
          <div>ID: {account.id}</div>
          <div>Название: {account.name}</div>
          <div>Баланс: {account.balance}</div>
          <div>
            {account.primary ? "Основной счёт" : ""}
          </div>

          <button onClick={() => deposit(account.id)}>
            Пополнить
          </button>
        </div>
      ))}

      <br />

      <button onClick={logout}>
        Выйти
      </button>
    </div>
  );
}