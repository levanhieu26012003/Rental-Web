// ProtectedRoute.js
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
  const user = useSelector((state) => state.auth.auth.user);

  if (user && !user.active) {
    return <Navigate to="/account-locked" replace />;
  }
  
  return children;
};

export default ProtectedRoute;
