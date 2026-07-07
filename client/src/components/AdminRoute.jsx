import { Navigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

function AdminRoute({ children }) {
  const { user, loading } = useAuth();

  if (loading) return null;

  if (!user || (user.role !== "ADMIN" && user.role !== "admin")) {
    return <Navigate to="/" replace />;
  }

  return children;
}

export default AdminRoute;
