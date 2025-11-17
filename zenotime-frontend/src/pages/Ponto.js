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
} from '@mui/material';
import api from '../services/api';

const Ponto = () => {
  const [registros, setRegistros] = useState([]);
  const [projetoId, setProjetoId] = useState('');
  const [observacoes, setObservacoes] = useState('');
  const [ultimoRegistro, setUltimoRegistro] = useState(null);
  const [message, setMessage] = useState('');

  useEffect(() => {
    carregarRegistros();
  }, []);

  const carregarRegistros = async () => {
    try {
      const response = await api.get('/ponto');
      setRegistros(response.data);
      if (response.data.length > 0) {
        const ultimo = response.data[response.data.length - 1];
        if (!ultimo.dataHoraSaida) {
          setUltimoRegistro(ultimo);
        }
      }
    } catch (error) {
      console.error('Erro ao carregar registros:', error);
    }
  };

  const registrarEntrada = async () => {
    try {
      const response = await api.post('/ponto/entrada', {
        dataHoraEntrada: new Date().toISOString(),
        projetoId: projetoId || null,
        observacoes,
        tipo: 'NORMAL',
      });
      setMessage('Entrada registrada com sucesso!');
      setProjetoId('');
      setObservacoes('');
      carregarRegistros();
    } catch (error) {
      setMessage('Erro ao registrar entrada: ' + (error.response?.data?.message || error.message));
    }
  };

  const registrarSaida = async () => {
    if (!ultimoRegistro) return;
    try {
      await api.post(`/ponto/saida/${ultimoRegistro.id}`);
      setMessage('Saída registrada com sucesso!');
      setUltimoRegistro(null);
      carregarRegistros();
    } catch (error) {
      setMessage('Erro ao registrar saída: ' + (error.response?.data?.message || error.message));
    }
  };

  return (
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
        {!ultimoRegistro ? (
          <Box>
            <TextField
              fullWidth
              label="ID do Projeto (opcional)"
              value={projetoId}
              onChange={(e) => setProjetoId(e.target.value)}
              sx={{ mb: 2 }}
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
            <Button variant="contained" onClick={registrarEntrada}>
              Registrar Entrada
            </Button>
          </Box>
        ) : (
          <Box>
            <Typography variant="body1" gutterBottom>
              Entrada registrada: {new Date(ultimoRegistro.dataHoraEntrada).toLocaleString('pt-BR')}
            </Typography>
            <Button variant="contained" color="secondary" onClick={registrarSaida} sx={{ mt: 2 }}>
              Registrar Saída
            </Button>
          </Box>
        )}
      </Paper>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Data/Hora Entrada</TableCell>
              <TableCell>Data/Hora Saída</TableCell>
              <TableCell>Horas Trabalhadas</TableCell>
              <TableCell>Projeto</TableCell>
              <TableCell>Observações</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {registros.map((registro) => (
              <TableRow key={registro.id}>
                <TableCell>
                  {registro.dataHoraEntrada
                    ? new Date(registro.dataHoraEntrada).toLocaleString('pt-BR')
                    : '-'}
                </TableCell>
                <TableCell>
                  {registro.dataHoraSaida
                    ? new Date(registro.dataHoraSaida).toLocaleString('pt-BR')
                    : '-'}
                </TableCell>
                <TableCell>{registro.horasTrabalhadas || '-'}</TableCell>
                <TableCell>{registro.projetoNome || '-'}</TableCell>
                <TableCell>{registro.observacoes || '-'}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default Ponto;

