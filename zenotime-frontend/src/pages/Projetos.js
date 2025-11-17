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

const Projetos = () => {
  const [projetos, setProjetos] = useState([]);
  const [empresas, setEmpresas] = useState([]);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [formData, setFormData] = useState({ nome: '', descricao: '', empresaId: '' });

  useEffect(() => {
    carregarProjetos();
    carregarEmpresas();
  }, []);

  const carregarProjetos = async () => {
    try {
      const response = await api.get('/projetos');
      setProjetos(response.data);
    } catch (error) {
      console.error('Erro ao carregar projetos:', error);
    }
  };

  const carregarEmpresas = async () => {
    try {
      const response = await api.get('/empresas');
      setEmpresas(response.data);
    } catch (error) {
      console.error('Erro ao carregar empresas:', error);
    }
  };

  const handleOpen = (projeto = null) => {
    if (projeto) {
      setEditing(projeto);
      setFormData({
        nome: projeto.nome,
        descricao: projeto.descricao || '',
        empresaId: projeto.empresa?.id || '',
      });
    } else {
      setEditing(null);
      setFormData({ nome: '', descricao: '', empresaId: '' });
    }
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setEditing(null);
    setFormData({ nome: '', descricao: '', empresaId: '' });
  };

  const handleSave = async () => {
    try {
      const data = {
        ...formData,
        empresa: { id: formData.empresaId },
      };
      if (editing) {
        await api.put(`/projetos/${editing.id}`, data);
      } else {
        await api.post('/projetos', { ...data, ativo: true });
      }
      handleClose();
      carregarProjetos();
    } catch (error) {
      console.error('Erro ao salvar projeto:', error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este projeto?')) {
      try {
        await api.delete(`/projetos/${id}`);
        carregarProjetos();
      } catch (error) {
        console.error('Erro ao excluir projeto:', error);
      }
    }
  };

  return (
    <Layout>
      <Container maxWidth="lg">
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
          <Typography variant="h4">Projetos</Typography>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => handleOpen()}
          >
            Novo Projeto
          </Button>
        </Box>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Nome</TableCell>
                <TableCell>Descrição</TableCell>
                <TableCell>Empresa</TableCell>
                <TableCell>Ações</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {projetos.map((projeto) => (
                <TableRow key={projeto.id}>
                  <TableCell>{projeto.nome}</TableCell>
                  <TableCell>{projeto.descricao || '-'}</TableCell>
                  <TableCell>{projeto.empresa?.nome || '-'}</TableCell>
                  <TableCell>
                    <IconButton onClick={() => handleOpen(projeto)}>
                      <EditIcon />
                    </IconButton>
                    <IconButton onClick={() => handleDelete(projeto.id)}>
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
            {editing ? 'Editar Projeto' : 'Novo Projeto'}
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
              <InputLabel>Empresa</InputLabel>
              <Select
                value={formData.empresaId}
                onChange={(e) => setFormData({ ...formData, empresaId: e.target.value })}
                label="Empresa"
              >
                {empresas.map((empresa) => (
                  <MenuItem key={empresa.id} value={empresa.id}>
                    {empresa.nome}
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

export default Projetos;

