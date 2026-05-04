import { useNavigate } from "react-router-dom";
import { GoogleLogin } from "@react-oauth/google";
import { setAuthToken } from "../auth/authStorage";
import { useEffect } from "react";
import { isAuthenticated } from "../auth/authStorage";

function LandingPage() {
    const navigate = useNavigate();
    const handleGoogleSuccess = (credentialResponse: { credential?: string }) => {
      if (!credentialResponse.credential) {
        return;
      }

      setAuthToken(credentialResponse.credential);
      navigate("/dashboard");
    };
    useEffect(() => {
      if (isAuthenticated()) {
        navigate("/dashboard");
      }
    }, [navigate]);
  return (
    <main style={styles.page}>
      <section style={styles.card}>
        <p style={styles.badge}>AI Life Operations System — Germany Edition</p>

        <h1 style={styles.title}>
          Manage German bureaucracy, documents, deadlines, and trusted information with AI.
        </h1>

        <p style={styles.description}>
          LifeOps helps users understand official letters, detect required actions,
          track deadlines, and verify internet content through a human-approved AI workflow.
        </p>

        <div style={styles.actions}>
          <GoogleLogin
            onSuccess={handleGoogleSuccess}
            onError={() => {
              console.error("Google login failed");
            }}
          />

          <button style={styles.secondaryButton}>
            Continue with Facebook
          </button>
        </div>

        <p style={styles.note}>
          Login is required to access dashboard, documents, AI tools, and task history.
        </p>
      </section>
    </main>
  );
}

const styles: Record<string, React.CSSProperties> = {
  page: {
    minHeight: "100vh",
    background: "#f5f7fb",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    padding: "24px",
    fontFamily: "Inter, system-ui, sans-serif",
  },
  card: {
    maxWidth: "880px",
    background: "#ffffff",
    borderRadius: "24px",
    padding: "56px",
    boxShadow: "0 20px 60px rgba(15, 23, 42, 0.08)",
  },
  badge: {
    fontSize: "14px",
    fontWeight: 600,
    color: "#2563eb",
    marginBottom: "20px",
  },
  title: {
    fontSize: "44px",
    lineHeight: 1.1,
    color: "#0f172a",
    marginBottom: "24px",
  },
  description: {
    fontSize: "18px",
    lineHeight: 1.7,
    color: "#475569",
    marginBottom: "32px",
  },
  actions: {
    display: "flex",
    gap: "16px",
    flexWrap: "wrap",
    marginBottom: "20px",
  },
  primaryButton: {
    background: "#2563eb",
    color: "#ffffff",
    border: "none",
    borderRadius: "12px",
    padding: "14px 22px",
    fontSize: "16px",
    fontWeight: 600,
    cursor: "pointer",
  },
  secondaryButton: {
    background: "#f8fafc",
    color: "#0f172a",
    border: "1px solid #cbd5e1",
    borderRadius: "12px",
    padding: "14px 22px",
    fontSize: "16px",
    fontWeight: 600,
    cursor: "not-allowed",
  },
  note: {
    fontSize: "14px",
    color: "#64748b",
  },
};

export default LandingPage;