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

const Sprints = () => {
  const [sprints, setSprints] = useState([]);
  const [times, setTimes] = useState([]);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [formData, setFormData] = useState({
    nome: '',
    dataInicio: '',
    dataFim: '',
    objetivo: '',
    timeId: '',
  });

  useEffect(() => {
    carregarSprints();
    carregarTimes();
  }, []);

  const carregarSprints = async () => {
    try {
      const response = await api.get('/sprints');
      setSprints(response.data);
    } catch (error) {
      console.error('Erro ao carregar sprints:', error);
    }
  };

  const carregarTimes = async () => {
    try {
      const response = await api.get('/times');
      setTimes(response.data);
    } catch (error) {
      console.error('Erro ao carregar times:', error);
    }
  };

  const handleOpen = (sprint = null) => {
    if (sprint) {
      setEditing(sprint);
      setFormData({
        nome: sprint.nome,
        dataInicio: sprint.dataInicio || '',
        dataFim: sprint.dataFim || '',
        objetivo: sprint.objetivo || '',
        timeId: sprint.time?.id || '',
      });
    } else {
      setEditing(null);
      setFormData({
        nome: '',
        dataInicio: '',
        dataFim: '',
        objetivo: '',
        timeId: '',
      });
    }
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setEditing(null);
    setFormData({
      nome: '',
      dataInicio: '',
      dataFim: '',
      objetivo: '',
      timeId: '',
    });
  };

  const handleSave = async () => {
    try {
      const data = {
        ...formData,
        time: { id: formData.timeId },
        status: 'PLANEJADA',
      };
      if (editing) {
        await api.put(`/sprints/${editing.id}`, data);
      } else {
        await api.post('/sprints', data);
      }
      handleClose();
      carregarSprints();
    } catch (error) {
      console.error('Erro ao salvar sprint:', error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir esta sprint?')) {
      try {
        await api.delete(`/sprints/${id}`);
        carregarSprints();
      } catch (error) {
        console.error('Erro ao excluir sprint:', error);
      }
    }
  };

  return (
    <Layout>
      <Container maxWidth="lg">
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
          <Typography variant="h4">Sprints</Typography>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => handleOpen()}
          >
            Nova Sprint
          </Button>
        </Box>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Nome</TableCell>
                <TableCell>Time</TableCell>
                <TableCell>Data Início</TableCell>
                <TableCell>Data Fim</TableCell>
                <TableCell>Status</TableCell>
                <TableCell>Ações</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {sprints.map((sprint) => (
                <TableRow key={sprint.id}>
                  <TableCell>{sprint.nome}</TableCell>
                  <TableCell>{sprint.time?.nome || '-'}</TableCell>
                  <TableCell>
                    {sprint.dataInicio
                      ? new Date(sprint.dataInicio).toLocaleDateString('pt-BR')
                      : '-'}
                  </TableCell>
                  <TableCell>
                    {sprint.dataFim
                      ? new Date(sprint.dataFim).toLocaleDateString('pt-BR')
                      : '-'}
                  </TableCell>
                  <TableCell>{sprint.status}</TableCell>
                  <TableCell>
                    <IconButton onClick={() => handleOpen(sprint)}>
                      <EditIcon />
                    </IconButton>
                    <IconButton onClick={() => handleDelete(sprint.id)}>
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
            {editing ? 'Editar Sprint' : 'Nova Sprint'}
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
              label="Objetivo"
              value={formData.objetivo}
              onChange={(e) => setFormData({ ...formData, objetivo: e.target.value })}
              sx={{ mt: 2 }}
              multiline
              rows={3}
            />
            <FormControl fullWidth sx={{ mt: 2 }}>
              <InputLabel>Time</InputLabel>
              <Select
                value={formData.timeId}
                onChange={(e) => setFormData({ ...formData, timeId: e.target.value })}
                label="Time"
              >
                {times.map((time) => (
                  <MenuItem key={time.id} value={time.id}>
                    {time.nome}
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

export default Sprints;

