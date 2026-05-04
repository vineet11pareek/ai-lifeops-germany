import { Navigate, Outlet } from "react-router-dom";
import { isAuthenticated } from "./authStorage";

function ProtectedRoute() {
  if (!isAuthenticated()) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
}

export default ProtectedRoute;