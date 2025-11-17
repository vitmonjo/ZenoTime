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
} from '@mui/material';
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon } from '@mui/icons-material';
import Layout from '../components/Layout';
import api from '../services/api';

const Empresas = () => {
  const [empresas, setEmpresas] = useState([]);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [formData, setFormData] = useState({ nome: '', cnpj: '' });

  useEffect(() => {
    carregarEmpresas();
  }, []);

  const carregarEmpresas = async () => {
    try {
      const response = await api.get('/empresas');
      setEmpresas(response.data);
    } catch (error) {
      console.error('Erro ao carregar empresas:', error);
    }
  };

  const handleOpen = (empresa = null) => {
    if (empresa) {
      setEditing(empresa);
      setFormData({ nome: empresa.nome, cnpj: empresa.cnpj || '' });
    } else {
      setEditing(null);
      setFormData({ nome: '', cnpj: '' });
    }
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setEditing(null);
    setFormData({ nome: '', cnpj: '' });
  };

  const handleSave = async () => {
    try {
      if (editing) {
        await api.put(`/empresas/${editing.id}`, formData);
      } else {
        await api.post('/empresas', { ...formData, ativo: true });
      }
      handleClose();
      carregarEmpresas();
    } catch (error) {
      console.error('Erro ao salvar empresa:', error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir esta empresa?')) {
      try {
        await api.delete(`/empresas/${id}`);
        carregarEmpresas();
      } catch (error) {
        console.error('Erro ao excluir empresa:', error);
      }
    }
  };

  return (
    <Layout>
      <Container maxWidth="lg">
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
          <Typography variant="h4">Empresas</Typography>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => handleOpen()}
          >
            Nova Empresa
          </Button>
        </Box>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Nome</TableCell>
                <TableCell>CNPJ</TableCell>
                <TableCell>Ações</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {empresas.map((empresa) => (
                <TableRow key={empresa.id}>
                  <TableCell>{empresa.nome}</TableCell>
                  <TableCell>{empresa.cnpj || '-'}</TableCell>
                  <TableCell>
                    <IconButton onClick={() => handleOpen(empresa)}>
                      <EditIcon />
                    </IconButton>
                    <IconButton onClick={() => handleDelete(empresa.id)}>
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <Dialog open={open} onClose={handleClose}>
          <DialogTitle>
            {editing ? 'Editar Empresa' : 'Nova Empresa'}
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
              label="CNPJ"
              value={formData.cnpj}
              onChange={(e) => setFormData({ ...formData, cnpj: e.target.value })}
              sx={{ mt: 2 }}
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

export default Empresas;

