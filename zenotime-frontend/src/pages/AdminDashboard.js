import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  Paper,
} from '@mui/material';
import {
  LineChart,
  Line,
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';
import Layout from '../components/Layout';
import api from '../services/api';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

const AdminDashboard = () => {
  const [dashboardData, setDashboardData] = useState({
    totalHoras: 0,
    funcionariosAtivos: 0,
    projetosAtivos: 0,
    produtividade: [],
    distribuicaoHoras: [],
    comparativoTimes: [],
  });

  useEffect(() => {
    // Simular dados do dashboard
    setDashboardData({
      totalHoras: 1240,
      funcionariosAtivos: 25,
      projetosAtivos: 8,
      produtividade: [
        { name: 'Seg', horas: 240 },
        { name: 'Ter', horas: 280 },
        { name: 'Qua', horas: 260 },
        { name: 'Qui', horas: 300 },
        { name: 'Sex', horas: 220 },
      ],
      distribuicaoHoras: [
        { name: 'Projeto A', value: 400 },
        { name: 'Projeto B', value: 300 },
        { name: 'Projeto C', value: 200 },
        { name: 'Projeto D', value: 100 },
      ],
      comparativoTimes: [
        { name: 'Time 1', horas: 320 },
        { name: 'Time 2', horas: 280 },
        { name: 'Time 3', horas: 240 },
      ],
    });
  }, []);

  return (
    <Layout>
      <Container maxWidth="lg">
        <Typography variant="h4" gutterBottom>
          Dashboard Administrativo
        </Typography>
        <Grid container spacing={3} sx={{ mt: 2 }}>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <CardContent>
                <Typography color="textSecondary" gutterBottom>
                  Total de Horas (Mês)
                </Typography>
                <Typography variant="h4">{dashboardData.totalHoras}h</Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <CardContent>
                <Typography color="textSecondary" gutterBottom>
                  Funcionários Ativos
                </Typography>
                <Typography variant="h4">{dashboardData.funcionariosAtivos}</Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <CardContent>
                <Typography color="textSecondary" gutterBottom>
                  Projetos em Andamento
                </Typography>
                <Typography variant="h4">{dashboardData.projetosAtivos}</Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <CardContent>
                <Typography color="textSecondary" gutterBottom>
                  Média Diária
                </Typography>
                <Typography variant="h4">248h</Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} md={8}>
            <Paper sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom>
                Produtividade Semanal
              </Typography>
              <ResponsiveContainer width="100%" height={300}>
                <LineChart data={dashboardData.produtividade}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Line type="monotone" dataKey="horas" stroke="#8884d8" />
                </LineChart>
              </ResponsiveContainer>
            </Paper>
          </Grid>
          <Grid item xs={12} md={4}>
            <Paper sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom>
                Distribuição de Horas por Projeto
              </Typography>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie
                    data={dashboardData.distribuicaoHoras}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {dashboardData.distribuicaoHoras.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </Paper>
          </Grid>
          <Grid item xs={12}>
            <Paper sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom>
                Comparativo de Produtividade entre Times
              </Typography>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={dashboardData.comparativoTimes}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="horas" fill="#8884d8" />
                </BarChart>
              </ResponsiveContainer>
            </Paper>
          </Grid>
        </Grid>
      </Container>
    </Layout>
  );
};

export default AdminDashboard;

