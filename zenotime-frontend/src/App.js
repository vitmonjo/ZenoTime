import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import AdminDashboard from './pages/AdminDashboard';
import Empresas from './pages/Empresas';
import Projetos from './pages/Projetos';
import Times from './pages/Times';
import Sprints from './pages/Sprints';
import Ponto from './pages/Ponto';
import Solicitacoes from './pages/Solicitacoes';
import { AuthProvider, useAuth } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

const queryClient = new QueryClient();

function AppRoutes() {
  const { user } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route
        path="/dashboard"
        element={
          <PrivateRoute>
            {user?.tipo === 'ADMINISTRADOR' ? <AdminDashboard /> : <Dashboard />}
          </PrivateRoute>
        }
      />
      <Route
        path="/empresas"
        element={
          <PrivateRoute requiredRole="ADMINISTRADOR">
            <Empresas />
          </PrivateRoute>
        }
      />
      <Route
        path="/projetos"
        element={
          <PrivateRoute requiredRole="ADMINISTRADOR">
            <Projetos />
          </PrivateRoute>
        }
      />
      <Route
        path="/times"
        element={
          <PrivateRoute requiredRole="ADMINISTRADOR">
            <Times />
          </PrivateRoute>
        }
      />
      <Route
        path="/sprints"
        element={
          <PrivateRoute requiredRole="ADMINISTRADOR">
            <Sprints />
          </PrivateRoute>
        }
      />
      <Route
        path="/ponto"
        element={
          <PrivateRoute>
            <Ponto />
          </PrivateRoute>
        }
      />
      <Route
        path="/solicitacoes"
        element={
          <PrivateRoute>
            <Solicitacoes />
          </PrivateRoute>
        }
      />
      <Route path="/" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthProvider>
          <Router>
            <AppRoutes />
          </Router>
        </AuthProvider>
      </ThemeProvider>
    </QueryClientProvider>
  );
}

export default App;

