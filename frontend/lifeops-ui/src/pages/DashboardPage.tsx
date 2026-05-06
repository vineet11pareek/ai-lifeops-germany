import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getCurrentUser, type UserResponse } from "../api/userApi";
import { askAiQuestion, type AiChatResponse } from "../api/aiApi";
import { clearAuthToken } from "../auth/authStorage";
const modules = [
  {
    title: "Bureaucracy Assistant",
    description: "Analyze German official letters, deadlines, and required actions.",
    status: "Planned",
  },
  {
    title: "Truth Layer",
    description: "Verify internet content, detect claims, and assess credibility.",
    status: "Planned",
  },
  {
    title: "Task Approval Layer",
    description: "Review AI-created task proposals before execution.",
    status: "Planned",
  },
];

function DashboardPage() {
    const [user, setUser] = useState<UserResponse | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const [question, setQuestion] = useState("");
    const [aiResponse, setAiResponse] = useState<AiChatResponse | null>(null);
    const [isAiLoading, setIsAiLoading] = useState(false);
    const [aiError, setAiError] = useState<string | null>(null);

    const handleLogout = () => {
      clearAuthToken();
      navigate("/");
    };

    const handleAskAi = async (event: React.FormEvent<HTMLFormElement>) => {
      event.preventDefault();

      if (!question.trim()) {
        setAiError("Please enter a question.");
        return;
      }

      try {
        setIsAiLoading(true);
        setAiError(null);
        setAiResponse(null);

        const response = await askAiQuestion(question.trim());
        setAiResponse(response);
        setQuestion("");
      } catch (err) {
        console.error(err);
        setAiError("Unable to process AI request. Please try again.");
      } finally {
        setIsAiLoading(false);
      }
    };
    useEffect(() => {
      async function loadUser() {
        try {
          const currentUser = await getCurrentUser();
          setUser(currentUser);
        } catch (err) {
          console.error(err);
          setError("Unable to load user profile.");
        } finally {
          setIsLoading(false);
        }
      }

      loadUser();
    }, []);
  return (
    <main style={styles.page}>
      <section style={styles.header}>
        <div>
          <p style={styles.badge}>Dashboard</p>
          <h1 style={styles.title}>
            Welcome back, {user?.name ?? "User"}
          </h1>
          <p style={styles.subtitle}>
            Manage your documents, AI queries, deadlines, and approved tasks from one place.
          </p>
        </div>

        <button style={styles.logoutButton} onClick={handleLogout}>
          Logout
        </button>
      </section>

      <section style={styles.grid}>
        <div style={styles.card}>
          <h2 style={styles.cardTitle}>Profile</h2>

            {isLoading && <p style={styles.emptyText}>Loading profile...</p>}

            {!isLoading && error && <p style={styles.errorText}>{error}</p>}

            {!isLoading && user && (
              <>
                <p style={styles.cardText}>Name: {user.name}</p>
                <p style={styles.cardText}>Email: {user.email}</p>
                <p style={styles.cardText}>Country: {user.country}</p>
                <p style={styles.cardText}>Provider: {user.provider}</p>
              </>
            )}
        </div>

        <div style={styles.card}>
          <h2 style={styles.cardTitle}>Recent Queries</h2>
          <p style={styles.emptyText}>No AI queries yet.</p>
        </div>

        <div style={styles.card}>
          <h2 style={styles.cardTitle}>Pending Tasks</h2>
          <p style={styles.emptyText}>No pending approvals.</p>
        </div>

        <div style={styles.card}>
          <h2 style={styles.cardTitle}>Upcoming Deadlines</h2>
          <p style={styles.emptyText}>No tracked deadlines yet.</p>
        </div>
      </section>

      <section style={styles.aiSection}>
        <div style={styles.aiCard}>
          <div>
            <p style={styles.badge}>AI Assistant</p>
            <h2 style={styles.sectionTitle}>Ask LifeOps AI</h2>
            <p style={styles.aiDescription}>
              Ask a question about German bureaucracy, documents, deadlines, or daily life operations.
            </p>
          </div>

          <form onSubmit={handleAskAi} style={styles.aiForm}>
            <textarea
              value={question}
              onChange={(event) => setQuestion(event.target.value)}
              placeholder="Example: Explain Anmeldung in Germany in simple English."
              style={styles.textarea}
              rows={5}
            />

            <button
              type="submit"
              style={{
                ...styles.primaryButton,
                opacity: isAiLoading ? 0.7 : 1,
                cursor: isAiLoading ? "not-allowed" : "pointer",
              }}
              disabled={isAiLoading}
            >
              {isAiLoading ? "Thinking..." : "Ask AI"}
            </button>
          </form>

          {aiError && <p style={styles.errorText}>{aiError}</p>}

          {aiResponse && (
            <div style={styles.answerBox}>
              <p style={styles.answerMeta}>
                Status: {aiResponse.status} · Provider: {aiResponse.provider} · Model: {aiResponse.model}
              </p>
              <h3 style={styles.answerTitle}>Answer</h3>
              <p style={styles.answerText}>{aiResponse.answer}</p>
            </div>
          )}
        </div>
      </section>

      <section style={styles.modulesSection}>
        <h2 style={styles.sectionTitle}>Available AI Tools</h2>

        <div style={styles.moduleGrid}>
          {modules.map((module) => (
            <article key={module.title} style={styles.moduleCard}>
              <div>
                <h3 style={styles.moduleTitle}>{module.title}</h3>
                <p style={styles.moduleDescription}>{module.description}</p>
              </div>
              <span style={styles.statusBadge}>{module.status}</span>
            </article>
          ))}
        </div>
      </section>
    </main>
  );
}

const styles: Record<string, React.CSSProperties> = {
  page: {
    minHeight: "100vh",
    background: "#f5f7fb",
    padding: "32px",
    fontFamily: "Inter, system-ui, sans-serif",
  },
  header: {
    maxWidth: "1180px",
    margin: "0 auto 32px",
    display: "flex",
    justifyContent: "space-between",
    gap: "24px",
    alignItems: "flex-start",
  },
  badge: {
    fontSize: "14px",
    fontWeight: 700,
    color: "#2563eb",
    marginBottom: "8px",
  },
  title: {
    fontSize: "38px",
    color: "#0f172a",
    margin: "0 0 12px",
  },
  subtitle: {
    fontSize: "17px",
    color: "#475569",
    maxWidth: "680px",
    lineHeight: 1.6,
  },
  logoutButton: {
    background: "#ffffff",
    border: "1px solid #cbd5e1",
    color: "#0f172a",
    borderRadius: "12px",
    padding: "12px 18px",
    fontWeight: 600,
    cursor: "pointer",
  },
  grid: {
    maxWidth: "1180px",
    margin: "0 auto",
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(240px, 1fr))",
    gap: "20px",
  },
  card: {
    background: "#ffffff",
    borderRadius: "20px",
    padding: "24px",
    boxShadow: "0 12px 36px rgba(15, 23, 42, 0.06)",
  },
  cardTitle: {
    fontSize: "18px",
    color: "#0f172a",
    marginBottom: "16px",
  },
  cardText: {
    color: "#475569",
    margin: "8px 0",
  },
  emptyText: {
    color: "#94a3b8",
  },
  modulesSection: {
    maxWidth: "1180px",
    margin: "32px auto 0",
  },
  sectionTitle: {
    fontSize: "24px",
    color: "#0f172a",
    marginBottom: "18px",
  },
  moduleGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))",
    gap: "20px",
  },
  moduleCard: {
    background: "#ffffff",
    borderRadius: "20px",
    padding: "24px",
    boxShadow: "0 12px 36px rgba(15, 23, 42, 0.06)",
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    gap: "20px",
  },
  moduleTitle: {
    fontSize: "19px",
    color: "#0f172a",
    marginBottom: "10px",
  },
  moduleDescription: {
    color: "#475569",
    lineHeight: 1.6,
  },
  statusBadge: {
    alignSelf: "flex-start",
    background: "#eff6ff",
    color: "#2563eb",
    borderRadius: "999px",
    padding: "6px 12px",
    fontSize: "13px",
    fontWeight: 700,
  },
  errorText: {
      color: "#dc2626",
  },
    aiSection: {
      maxWidth: "1180px",
      margin: "32px auto 0",
    },
    aiCard: {
      background: "#ffffff",
      borderRadius: "20px",
      padding: "28px",
      boxShadow: "0 12px 36px rgba(15, 23, 42, 0.06)",
    },
    aiDescription: {
      color: "#475569",
      lineHeight: 1.6,
      marginBottom: "20px",
    },
    aiForm: {
      display: "flex",
      flexDirection: "column",
      gap: "14px",
    },
    textarea: {
      width: "100%",
      borderRadius: "14px",
      border: "1px solid #cbd5e1",
      padding: "14px",
      fontSize: "15px",
      fontFamily: "Inter, system-ui, sans-serif",
      resize: "vertical",
      boxSizing: "border-box",
    },
    primaryButton: {
      alignSelf: "flex-start",
      background: "#2563eb",
      color: "#ffffff",
      border: "none",
      borderRadius: "12px",
      padding: "12px 18px",
      fontWeight: 700,
      cursor: "pointer",
    },
    answerBox: {
      marginTop: "24px",
      background: "#f8fafc",
      border: "1px solid #e2e8f0",
      borderRadius: "16px",
      padding: "20px",
    },
    answerMeta: {
      fontSize: "13px",
      color: "#64748b",
      marginBottom: "10px",
    },
    answerTitle: {
      fontSize: "18px",
      color: "#0f172a",
      marginBottom: "10px",
    },
    answerText: {
      color: "#334155",
      lineHeight: 1.7,
      whiteSpace: "pre-wrap",
    },
};

export default DashboardPage;