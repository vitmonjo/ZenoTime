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
  IconButton,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
} from '@mui/material';
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon } from '@mui/icons-material';
import Layout from '../components/Layout';
import api from '../services/api';

const Times = () => {
  const [times, setTimes] = useState([]);
  const [projetos, setProjetos] = useState([]);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [formData, setFormData] = useState({ nome: '', descricao: '', projetoId: '' });

  useEffect(() => {
    carregarTimes();
    carregarProjetos();
  }, []);

  const carregarTimes = async () => {
    try {
      const response = await api.get('/times');
      setTimes(response.data);
    } catch (error) {
      console.error('Erro ao carregar times:', error);
    }
  };

  const carregarProjetos = async () => {
    try {
      const response = await api.get('/projetos');
      setProjetos(response.data);
    } catch (error) {
      console.error('Erro ao carregar projetos:', error);
    }
  };

  const handleOpen = (time = null) => {
    if (time) {
      setEditing(time);
      setFormData({
        nome: time.nome,
        descricao: time.descricao || '',
        projetoId: time.projeto?.id || '',
      });
    } else {
      setEditing(null);
      setFormData({ nome: '', descricao: '', projetoId: '' });
    }
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setEditing(null);
    setFormData({ nome: '', descricao: '', projetoId: '' });
  };

  const handleSave = async () => {
    try {
      const data = {
        ...formData,
        projeto: { id: formData.projetoId },
      };
      if (editing) {
        await api.put(`/times/${editing.id}`, data);
      } else {
        await api.post('/times', { ...data, ativo: true });
      }
      handleClose();
      carregarTimes();
    } catch (error) {
      console.error('Erro ao salvar time:', error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este time?')) {
      try {
        await api.delete(`/times/${id}`);
        carregarTimes();
      } catch (error) {
        console.error('Erro ao excluir time:', error);
      }
    }
  };

  return (
    <Layout>
      <Container maxWidth="lg">
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
          <Typography variant="h4">Times</Typography>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => handleOpen()}
          >
            Novo Time
          </Button>
        </Box>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Nome</TableCell>
                <TableCell>Descrição</TableCell>
                <TableCell>Projeto</TableCell>
                <TableCell>Ações</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {times.map((time) => (
                <TableRow key={time.id}>
                  <TableCell>{time.nome}</TableCell>
                  <TableCell>{time.descricao || '-'}</TableCell>
                  <TableCell>{time.projeto?.nome || '-'}</TableCell>
                  <TableCell>
                    <IconButton onClick={() => handleOpen(time)}>
                      <EditIcon />
                    </IconButton>
                    <IconButton onClick={() => handleDelete(time.id)}>
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
          <DialogTitle>
            {editing ? 'Editar Time' : 'Novo Time'}
          </DialogTitle>
          <DialogContent>
            <TextField
              fullWidth
              label="Nome"
              value={formData.nome}
              onChange={(e) => setFormData({ ...formData, nome: e.target.value })}
              sx={{ mt: 2 }}
            />
            <TextField
              fullWidth
              label="Descrição"
              value={formData.descricao}
              onChange={(e) => setFormData({ ...formData, descricao: e.target.value })}
              sx={{ mt: 2 }}
              multiline
              rows={3}
            />
            <FormControl fullWidth sx={{ mt: 2 }}>
              <InputLabel>Projeto</InputLabel>
              <Select
                value={formData.projetoId}
                onChange={(e) => setFormData({ ...formData, projetoId: e.target.value })}
                label="Projeto"
              >
                {projetos.map((projeto) => (
                  <MenuItem key={projeto.id} value={projeto.id}>
                    {projeto.nome}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
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

export default Times;

