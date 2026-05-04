import { useNavigate } from "react-router-dom";
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
    const navigate = useNavigate();

    const handleLogout = () => {
      localStorage.removeItem("lifeops_google_id_token");
      navigate("/");
    };
  return (
    <main style={styles.page}>
      <section style={styles.header}>
        <div>
          <p style={styles.badge}>Dashboard</p>
          <h1 style={styles.title}>Welcome back, Vineet</h1>
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
          <p style={styles.cardText}>Name: Vineet Pareek</p>
          <p style={styles.cardText}>Country: Germany</p>
          <p style={styles.cardText}>Account type: Demo</p>
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
};

export default DashboardPage;