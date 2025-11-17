import React, { useState, useEffect } from 'react';
import {
  Button,
  TextField,
  Paper,
  Typography,
  Box,
  Container,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Chip,
} from '@mui/material';
import { Add as AddIcon } from '@mui/icons-material';
import Layout from '../components/Layout';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';

const Solicitacoes = () => {
  const { user } = useAuth();
  const [solicitacoes, setSolicitacoes] = useState([]);
  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState({
    tipo: 'FERIAS',
    dataInicio: '',
    dataFim: '',
    justificativa: '',
  });

  useEffect(() => {
    carregarSolicitacoes();
  }, []);

  const carregarSolicitacoes = async () => {
    try {
      const response = await api.get('/solicitacoes');
      setSolicitacoes(response.data);
    } catch (error) {
      console.error('Erro ao carregar solicitações:', error);
    }
  };

  const handleOpen = () => {
    setFormData({
      tipo: 'FERIAS',
      dataInicio: '',
      dataFim: '',
      justificativa: '',
    });
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setFormData({
      tipo: 'FERIAS',
      dataInicio: '',
      dataFim: '',
      justificativa: '',
    });
  };

  const handleSave = async () => {
    try {
      await api.post('/solicitacoes', formData);
      handleClose();
      carregarSolicitacoes();
    } catch (error) {
      console.error('Erro ao criar solicitação:', error);
    }
  };

  const handleAprovar = async (id) => {
    try {
      await api.put(`/solicitacoes/${id}/aprovar`);
      carregarSolicitacoes();
    } catch (error) {
      console.error('Erro ao aprovar solicitação:', error);
    }
  };

  const handleRejeitar = async (id) => {
    try {
      await api.put(`/solicitacoes/${id}/rejeitar`);
      carregarSolicitacoes();
    } catch (error) {
      console.error('Erro ao rejeitar solicitação:', error);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'APROVADA':
        return 'success';
      case 'REJEITADA':
        return 'error';
      default:
        return 'warning';
    }
  };

  return (
    <Layout>
      <Container maxWidth="lg">
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
          <Typography variant="h4">Solicitações</Typography>
          {user?.tipo === 'FUNCIONARIO' && (
            <Button
              variant="contained"
              startIcon={<AddIcon />}
              onClick={handleOpen}
            >
              Nova Solicitação
            </Button>
          )}
        </Box>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Funcionário</TableCell>
                <TableCell>Tipo</TableCell>
                <TableCell>Data Início</TableCell>
                <TableCell>Data Fim</TableCell>
                <TableCell>Status</TableCell>
                {user?.tipo === 'ADMINISTRADOR' && <TableCell>Ações</TableCell>}
              </TableRow>
            </TableHead>
            <TableBody>
              {solicitacoes.map((solicitacao) => (
                <TableRow key={solicitacao.id}>
                  <TableCell>{solicitacao.funcionario?.nome || '-'}</TableCell>
                  <TableCell>{solicitacao.tipo}</TableCell>
                  <TableCell>
                    {solicitacao.dataInicio
                      ? new Date(solicitacao.dataInicio).toLocaleDateString('pt-BR')
                      : '-'}
                  </TableCell>
                  <TableCell>
                    {solicitacao.dataFim
                      ? new Date(solicitacao.dataFim).toLocaleDateString('pt-BR')
                      : '-'}
                  </TableCell>
                  <TableCell>
                    <Chip
                      label={solicitacao.status}
                      color={getStatusColor(solicitacao.status)}
                      size="small"
                    />
                  </TableCell>
                  {user?.tipo === 'ADMINISTRADOR' && solicitacao.status === 'PENDENTE' && (
                    <TableCell>
                      <Button
                        size="small"
                        color="success"
                        onClick={() => handleAprovar(solicitacao.id)}
                      >
                        Aprovar
                      </Button>
                      <Button
                        size="small"
                        color="error"
                        onClick={() => handleRejeitar(solicitacao.id)}
                      >
                        Rejeitar
                      </Button>
                    </TableCell>
                  )}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
          <DialogTitle>Nova Solicitação</DialogTitle>
          <DialogContent>
            <FormControl fullWidth sx={{ mt: 2 }}>
              <InputLabel>Tipo</InputLabel>
              <Select
                value={formData.tipo}
                onChange={(e) => setFormData({ ...formData, tipo: e.target.value })}
                label="Tipo"
              >
                <MenuItem value="FERIAS">Férias</MenuItem>
                <MenuItem value="ATESTADO">Atestado</MenuItem>
              </Select>
            </FormControl>
            <TextField
              fullWidth
              label="Data Início"
              type="date"
              value={formData.dataInicio}
              onChange={(e) => setFormData({ ...formData, dataInicio: e.target.value })}
              sx={{ mt: 2 }}
              InputLabelProps={{ shrink: true }}
            />
            <TextField
              fullWidth
              label="Data Fim"
              type="date"
              value={formData.dataFim}
              onChange={(e) => setFormData({ ...formData, dataFim: e.target.value })}
              sx={{ mt: 2 }}
              InputLabelProps={{ shrink: true }}
            />
            <TextField
              fullWidth
              label="Justificativa"
              value={formData.justificativa}
              onChange={(e) => setFormData({ ...formData, justificativa: e.target.value })}
              sx={{ mt: 2 }}
              multiline
              rows={3}
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClose}>Cancelar</Button>
            <Button onClick={handleSave} variant="contained">
              Salvar
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
    </Layout>
  );
};

export default Solicitacoes;

