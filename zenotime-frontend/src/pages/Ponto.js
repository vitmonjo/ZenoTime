import React, { useState, useEffect } from 'react';
import {
  Button,
  TextField,
  Paper,
  Typography,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Alert,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Chip,
} from '@mui/material';
import Layout from '../components/Layout';
import api from '../services/api';

const Ponto = () => {
  const [registros, setRegistros] = useState([]);
  const [projetoId, setProjetoId] = useState('');
  const [horasTrabalhadas, setHorasTrabalhadas] = useState('');
  const [dataTrabalho, setDataTrabalho] = useState(new Date().toISOString().split('T')[0]);
  const [observacoes, setObservacoes] = useState('');
  const [message, setMessage] = useState('');
  const [empresa, setEmpresa] = useState('');
  const [projetos, setProjetos] = useState([]);

  useEffect(() => {
    carregarDadosIniciais();
  }, []);

  const carregarDadosIniciais = async () => {
    try {
      await Promise.all([
        carregarRegistros(),
        carregarEmpresa(),
        carregarProjetos()
      ]);
    } catch (error) {
      console.error('Erro ao carregar dados iniciais:', error);
    }
  };

  const carregarRegistros = async () => {
    try {
      const response = await api.get('/ponto');
      setRegistros(response.data);
    } catch (error) {
      console.error('Erro ao carregar registros:', error);
    }
  };

  const carregarEmpresa = async () => {
    try {
      const response = await api.get('/usuarios/minha-empresa');
      setEmpresa(response.data);
    } catch (error) {
      console.error('Erro ao carregar empresa:', error);
    }
  };

  const carregarProjetos = async () => {
    try {
      const response = await api.get('/usuarios/meus-projetos');
      setProjetos(response.data);
    } catch (error) {
      console.error('Erro ao carregar projetos:', error);
    }
  };

  const registrarHoras = async () => {
    try {
      const response = await api.post('/ponto/registrar-horas', {
        projetoId: parseInt(projetoId),
        dataTrabalho: dataTrabalho,
        horasTrabalhadas: parseFloat(horasTrabalhadas),
        observacoes,
        tipo: 'NORMAL',
      });
      setMessage('Horas registradas com sucesso!');
      setProjetoId('');
      setHorasTrabalhadas('');
      setObservacoes('');
      setDataTrabalho(new Date().toISOString().split('T')[0]);
      carregarRegistros();
    } catch (error) {
      setMessage('Erro ao registrar horas: ' + (error.response?.data?.message || error.message));
    }
  };


  return (
    <Layout>
      <Box>
      {message && (
        <Alert severity={message.includes('sucesso') ? 'success' : 'error'} sx={{ mb: 2 }}>
          {message}
        </Alert>
      )}
      <Paper sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6" gutterBottom>
          Registrar Ponto
        </Typography>
        {empresa && (
          <Box sx={{ mb: 2 }}>
            <Chip label={`Empresa: ${empresa}`} color="primary" variant="outlined" />
          </Box>
        )}
        <Box>
          <FormControl fullWidth sx={{ mb: 2 }} required>
            <InputLabel>Projeto</InputLabel>
            <Select
              value={projetoId}
              onChange={(e) => setProjetoId(e.target.value)}
              label="Projeto"
            >
              {projetos.map((projeto) => (
                <MenuItem key={projeto.id} value={projeto.id}>
                  {projeto.nome}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <TextField
            fullWidth
            label="Data do Trabalho"
            type="date"
            value={dataTrabalho}
            onChange={(e) => setDataTrabalho(e.target.value)}
            sx={{ mb: 2 }}
            InputLabelProps={{
              shrink: true,
            }}
            required
          />

          <TextField
            fullWidth
            label="Horas Trabalhadas (ex: 8.5)"
            value={horasTrabalhadas}
            onChange={(e) => setHorasTrabalhadas(e.target.value)}
            sx={{ mb: 2 }}
            type="number"
            inputProps={{ step: "0.5", min: "0.5", max: "24" }}
            required
          />

          <TextField
            fullWidth
            label="Observações (opcional)"
            value={observacoes}
            onChange={(e) => setObservacoes(e.target.value)}
            sx={{ mb: 2 }}
            multiline
            rows={3}
          />

          <Button
            variant="contained"
            onClick={registrarHoras}
            disabled={!projetoId || !horasTrabalhadas || !dataTrabalho}
          >
            Registrar Horas Trabalhadas
          </Button>
        </Box>
      </Paper>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Data</TableCell>
              <TableCell>Projeto</TableCell>
              <TableCell>Horas Trabalhadas</TableCell>
              <TableCell>Observações</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {registros.map((registro) => (
              <TableRow key={registro.id}>
                <TableCell>
                  {registro.dataTrabalho
                    ? (() => {
                        try {
                          // Tenta diferentes formatos de parsing
                          let date;
                          if (registro.dataTrabalho.includes('T')) {
                            // Formato ISO completo
                            date = new Date(registro.dataTrabalho);
                          } else {
                            // Formato YYYY-MM-DD, adiciona tempo para evitar problemas de timezone
                            date = new Date(registro.dataTrabalho + 'T12:00:00');
                          }

                          if (!isNaN(date.getTime())) {
                            return date.toLocaleDateString('pt-BR');
                          } else {
                            return registro.dataTrabalho; // Mostra a string original se não conseguir parsear
                          }
                        } catch (e) {
                          return registro.dataTrabalho; // Mostra a string original em caso de erro
                        }
                      })()
                    : '-'}
                </TableCell>
                <TableCell>{registro.projetoNome || '-'}</TableCell>
                <TableCell>
                  {registro.horasTrabalhadas ? Number(registro.horasTrabalhadas).toFixed(1) : '-'}
                </TableCell>
                <TableCell>{registro.observacoes || '-'}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      </Box>
    </Layout>
  );
};

export default Ponto;

