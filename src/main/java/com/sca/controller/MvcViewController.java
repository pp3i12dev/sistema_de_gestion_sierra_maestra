package com.sca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindException;
import javax.servlet.http.HttpServletRequest;

import com.sca.model.Pedido;
import com.sca.model.Lote;
import com.sca.model.Barril;
import com.sca.service.impl.PedidoServiceImpl;
import com.sca.service.impl.LoteServiceImpl;

@Controller
public class MvcViewController {

    // Helper: unwrap java.util.Optional values returned inside service Respuesta.data
    private Object unwrap(Object maybeOptional) {
        if (maybeOptional instanceof java.util.Optional) {
            try {
                return ((java.util.Optional<?>) maybeOptional).orElse(null);
            } catch (Exception e) {
                return null;
            }
        }
        return maybeOptional;
    }

    @Autowired
    PedidoServiceImpl pedidoService;

    @Autowired
    LoteServiceImpl loteService;

    @Autowired
    com.sca.service.impl.ClienteServiceImpl clienteService;

    @Autowired
    com.sca.service.impl.CervezaServiceImpl cervezaService;

    @Autowired
    com.sca.service.impl.BarrilServiceImpl barrilService;

    @Autowired
    com.sca.service.impl.AsociadosServiceImpl asociadosService;
    
    @Autowired
    com.sca.service.impl.MaduradorServiceImpl maduradorService;

    @Autowired
    com.sca.service.impl.AsistenciaServiceImpl asistenciaService;

    @Autowired
    com.sca.service.impl.AsistenciaTotalServiceImp asistenciaTotalService;

    @Autowired
    com.sca.service.impl.AccesorioServiceImpl accesorioService;
    
    @Autowired
    com.sca.service.impl.CategoriaServiceImpl categoriaService;

    @Autowired
    com.sca.service.impl.MesServiceImpl mesService;

    @Autowired
    com.sca.service.impl.SueldoBasicoServiceImpl sueldoBasicoService;

    @Autowired
    com.sca.service.impl.PorcentajeMesServiceImpl porcentajeMesService;

    @Autowired
    com.sca.service.impl.FirmaServiceImpl firmaService;

    @GetMapping({"/dashboard"})
    public String home(Model model) {
        model.addAttribute("title", "Sistema - Inicio");
        return "home";
    }

    // Pedidos
    @GetMapping("/pedidos")
    public String pedidosIndex(Model model) {
        model.addAttribute("title", "Pedidos");
        return "pedidos/index";
    }

    @GetMapping("/pedidos/list")
    public String pedidosList(Model model) {
        model.addAttribute("items", pedidoService.findAll().getData());
        return "pedidos/fragments :: lista";
    }

    @GetMapping("/pedidos/form")
    public String pedidoForm(Model model) {
        model.addAttribute("pedido", new Pedido());
    model.addAttribute("clientes", clienteService.findAll().getData());
    model.addAttribute("cervezas", cervezaService.findAll().getData());
    model.addAttribute("asociados", asociadosService.findAll().getData());
        return "pedidos/fragments :: form";
    }

    @GetMapping("/pedidos/{id}")
    public String pedidoById(@PathVariable Long id, Model model) {
    model.addAttribute("pedido", unwrap(pedidoService.findById(id).getData()));
    model.addAttribute("clientes", clienteService.findAll().getData());
    model.addAttribute("cervezas", cervezaService.findAll().getData());
    model.addAttribute("asociados", asociadosService.findAll().getData());
        return "pedidos/fragments :: form";
    }

    @GetMapping("/pedidos/view/{id}")
    public String pedidoViewById(@PathVariable Long id, Model model) {
    model.addAttribute("pedido", unwrap(pedidoService.findById(id).getData()));
        return "pedidos/fragments :: view";
    }

    @PostMapping("/pedidos/save")
    public String savePedido(Pedido pedido, Model model) {
        try {
            BindException be = new BindException(pedido, "pedido");
            pedidoService.save(pedido, be);
        } catch (Exception e) {
            // log si se desea
        }
        model.addAttribute("items", pedidoService.findAll().getData());
        return "pedidos/fragments :: lista";
    }

    // Lotes
    @GetMapping("/lotes")
    public String lotesIndex(Model model) {
        model.addAttribute("title", "Lotes");
        return "lotes/index";
    }

    @GetMapping("/lotes/list")
    public String lotesList(Model model) {
        model.addAttribute("items", loteService.findAll().getData());
        return "lotes/fragments :: lista";
    }

    @GetMapping("/lotes/form")
    public String loteForm(Model model) {
        model.addAttribute("lote", new Lote());
    model.addAttribute("cervezas", cervezaService.findAll().getData());
        return "lotes/fragments :: form";
    }

    @GetMapping("/lotes/{id}")
    public String loteById(@PathVariable Long id, Model model) {
    model.addAttribute("lote", unwrap(loteService.findById(id).getData()));
    model.addAttribute("cervezas", cervezaService.findAll().getData());
        return "lotes/fragments :: form";
    }

    @GetMapping("/lotes/view/{id}")
    public String loteViewById(@PathVariable Long id, Model model) {
    model.addAttribute("lote", unwrap(loteService.findById(id).getData()));
        return "lotes/fragments :: view";
    }

    @PostMapping("/lotes/save")
    public String saveLote(Lote lote, Model model, HttpServletRequest request) {
        try {
            // If the form submitted a nested cerveza.id, load the managed Cerveza entity
            if (lote != null && lote.getCerveza() != null && lote.getCerveza().getId() != null) {
                try {
                    Object c = cervezaService.findById(lote.getCerveza().getId()).getData();
                    if (c instanceof com.sca.model.Cerveza) {
                        lote.setCerveza((com.sca.model.Cerveza) c);
                    } else {
                        lote.setCerveza(null);
                    }
                } catch (Exception ex) {
                    System.out.println("[DEBUG] failed to resolve cerveza id=" + lote.getCerveza().getId() + " : " + ex.getMessage());
                    lote.setCerveza(null);
                }
            }
            // Fallback: sometimes the binder doesn't instantiate nested bean; check request params
            if (lote != null && (lote.getCerveza() == null || lote.getCerveza().getId() == null)) {
                String cervezaIdStr = request.getParameter("cerveza.id");
                if (cervezaIdStr == null) {
                    cervezaIdStr = request.getParameter("cerveza");
                }
                if (cervezaIdStr != null && !cervezaIdStr.trim().isEmpty()) {
                    try {
                        Long cid = Long.parseLong(cervezaIdStr);
                        Object c = cervezaService.findById(cid).getData();
                        if (c instanceof com.sca.model.Cerveza) {
                            lote.setCerveza((com.sca.model.Cerveza) c);
                        } else {
                            lote.setCerveza(new com.sca.model.Cerveza());
                            lote.getCerveza().setId(cid);
                        }
                    } catch (Exception ex2) {
                        System.out.println("[DEBUG] fallback failed to parse/resolve cerveza id='" + cervezaIdStr + "' : " + ex2.getMessage());
                    }
                }
            }

            BindException be = new BindException(lote, "lote");
            // capture response to log status/body for debugging
            try {
                org.springframework.http.ResponseEntity<Object> resp = loteService.save(lote, be);
                System.out.println("[DEBUG] saveLote response status=" + resp.getStatusCode() + " body=" + resp.getBody());
            } catch (Exception inner) {
                System.out.println("[DEBUG] saveLote threw: " + inner.getMessage());
            }
        } catch (Exception e) {
            // log si se desea
            System.out.println("[DEBUG] outer saveLote exception: " + e.getMessage());
        }
        model.addAttribute("items", loteService.findAll().getData());
        return "lotes/fragments :: lista";
    }

    // Debug: return raw lotes as JSON (only in dev)
    @GetMapping("/debug/lotes")
    @org.springframework.web.bind.annotation.ResponseBody
    public Object debugLotes() {
        return java.util.Collections.singletonMap("data", loteService.findAll().getData());
    }

    // Otros módulos - vistas index básicas
    @GetMapping("/clientes")
    public String clientesIndex(Model model) {
        model.addAttribute("title", "Clientes");
        return "clientes/index";
    }

    @GetMapping("/clientes/list")
    public String clientesList(Model model) {
        model.addAttribute("items", clienteService.findAll().getData());
        return "clientes/fragments :: lista";
    }

    @GetMapping("/clientes/form")
    public String clienteForm(Model model) {
        model.addAttribute("cliente", new com.sca.model.Cliente());
        return "clientes/fragments :: form";
    }

    @GetMapping("/clientes/{id}")
    public String clienteById(@PathVariable Long id, Model model) {
    model.addAttribute("cliente", unwrap(clienteService.findById(id).getData()));
        return "clientes/fragments :: form";
    }

    @GetMapping("/clientes/view/{id}")
    public String clienteViewById(@PathVariable Long id, Model model) {
    model.addAttribute("cliente", unwrap(clienteService.findById(id).getData()));
        return "clientes/fragments :: view";
    }

    @PostMapping("/clientes/save")
    public String saveCliente(com.sca.model.Cliente cliente, Model model) {
        try {
            // Debug: print incoming cliente fields to help diagnose binding/validation issues
            try {
                System.out.println("[DEBUG] saveCliente called with cliente=" + cliente);
            } catch (Exception x) { /* ignore */ }
            BindException be = new BindException(cliente, "cliente");
            // capture response to check service result
            try {
                org.springframework.http.ResponseEntity<Object> resp = clienteService.save(cliente, be);
                System.out.println("[DEBUG] saveCliente response status=" + resp.getStatusCode() + " body=" + resp.getBody());
            } catch (Exception inner) {
                System.out.println("[DEBUG] clienteService.save threw: " + inner.getMessage());
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] saveCliente outer exception: " + e.getMessage());
        }
        model.addAttribute("items", clienteService.findAll().getData());
        return "clientes/fragments :: lista";
    }

    @GetMapping("/cervezas")
    public String cervezasIndex(Model model) {
        model.addAttribute("title", "Cervezas");
        return "cervezas/index";
    }

    @GetMapping("/cervezas/list")
    public String cervezasList(Model model) {
        model.addAttribute("items", cervezaService.findAll().getData());
        return "cervezas/fragments :: lista";
    }

    @GetMapping("/cervezas/form")
    public String cervezaForm(Model model) {
        model.addAttribute("cerveza", new com.sca.model.Cerveza());
        return "cervezas/fragments :: form";
    }

    @GetMapping("/cervezas/{id}")
    public String cervezaById(@PathVariable Long id, Model model) {
    model.addAttribute("cerveza", unwrap(cervezaService.findById(id).getData()));
        return "cervezas/fragments :: form";
    }

    @GetMapping("/cervezas/view/{id}")
    public String cervezaViewById(@PathVariable Long id, Model model) {
    model.addAttribute("cerveza", unwrap(cervezaService.findById(id).getData()));
        return "cervezas/fragments :: view";
    }

    @PostMapping("/cervezas/save")
    public String saveCerveza(com.sca.model.Cerveza cerveza, Model model) {
        try {
            // Ensure required fields have sensible defaults to avoid validation failures
            if (cerveza.getTipoCerveza() == null || cerveza.getTipoCerveza().trim().isEmpty()) {
                cerveza.setTipoCerveza("Otro");
            }
            if (cerveza.getGradoAlcoholico() == null) {
                cerveza.setGradoAlcoholico(4.5);
            }
            if (cerveza.getAmargorIbu() == null) {
                cerveza.setAmargorIbu(10.0);
            }
            if (cerveza.getEstado() == null || cerveza.getEstado().trim().isEmpty()) {
                cerveza.setEstado("Disponible");
            }
            if (cerveza.getPrecioPorLitro() == null) {
                cerveza.setPrecioPorLitro(0.0);
            }
            BindException be = new BindException(cerveza, "cerveza");
            cervezaService.save(cerveza, be);
        } catch (Exception e) {
            // ignore for now
        }
        model.addAttribute("items", cervezaService.findAll().getData());
        return "cervezas/fragments :: lista";
    }

    @GetMapping("/categorias")
    public String categoriasIndex(Model model) {
        model.addAttribute("title", "Categorías");
        return "categorias/index";
    }
    
    @GetMapping("/categorias/list")
    public String categoriasList(Model model) {
        model.addAttribute("items", categoriaService.findAll().getData());
        return "categorias/fragments :: lista";
    }

    @GetMapping("/categorias/form")
    public String categoriaForm(Model model) {
        model.addAttribute("categoria", new com.sca.model.Categoria());
        return "categorias/fragments :: form";
    }

    @GetMapping("/categorias/{id}")
    public String categoriaById(@PathVariable Long id, Model model) {
    model.addAttribute("categoria", unwrap(categoriaService.findById(id).getData()));
        return "categorias/fragments :: form";
    }

    @GetMapping("/categorias/view/{id}")
    public String categoriaViewById(@PathVariable Long id, Model model) {
    model.addAttribute("categoria", unwrap(categoriaService.findById(id).getData()));
        return "categorias/fragments :: view";
    }

    @PostMapping("/categorias/save")
    public String saveCategoria(com.sca.model.Categoria categoria, Model model) {
        try {
            BindException be = new BindException(categoria, "categoria");
            categoriaService.save(categoria, be);
        } catch (Exception e) { }
        model.addAttribute("items", categoriaService.findAll().getData());
        return "categorias/fragments :: lista";
    }

    @GetMapping("/barriles")
    public String barrilesIndex(Model model) {
        model.addAttribute("title", "Barriles");
        return "barriles/index";
    }

    @GetMapping("/barriles/list")
    public String barrilesList(Model model) {
        model.addAttribute("items", barrilService.findAll().getData());
        return "barriles/fragments :: lista";
    }

    @GetMapping("/barriles/form")
    public String barrilForm(Model model) {
        model.addAttribute("barril", new Barril());
        model.addAttribute("lotes", loteService.findAll().getData());
        return "barriles/fragments :: form";
    }

    @GetMapping("/barriles/{id}")
    public String barrilById(@PathVariable Long id, Model model) {
    model.addAttribute("barril", unwrap(barrilService.findById(id).getData()));
        model.addAttribute("lotes", loteService.findAll().getData());
        return "barriles/fragments :: form";
    }

    @GetMapping("/barriles/view/{id}")
    public String barrilViewById(@PathVariable Long id, Model model) {
    model.addAttribute("barril", unwrap(barrilService.findById(id).getData()));
        return "barriles/fragments :: view";
    }

    @PostMapping("/barriles/save")
    public String saveBarril(Barril barril, Model model) {
        try {
            BindException be = new BindException(barril, "barril");
            barrilService.save(barril, be);
        } catch (Exception e) {
            // ignore
        }
        model.addAttribute("items", barrilService.findAll().getData());
        return "barriles/fragments :: lista";
    }

    @GetMapping("/accesorios")
    public String accesoriosIndex(Model model) {
        model.addAttribute("title", "Accesorios");
        return "accesorios/index";
    }
    
    @GetMapping("/accesorios/list")
    public String accesoriosList(Model model) {
        model.addAttribute("items", accesorioService.findAll().getData());
        return "accesorios/fragments :: lista";
    }

    @GetMapping("/accesorios/form")
    public String accesorioForm(Model model) {
        model.addAttribute("accesorio", new com.sca.model.Accesorio());
        return "accesorios/fragments :: form";
    }

    @GetMapping("/accesorios/{id}")
    public String accesorioById(@PathVariable Long id, Model model) {
    model.addAttribute("accesorio", unwrap(accesorioService.findById(id).getData()));
        return "accesorios/fragments :: form";
    }

    @GetMapping("/accesorios/view/{id}")
    public String accesorioViewById(@PathVariable Long id, Model model) {
        model.addAttribute("accesorio", unwrap(accesorioService.findById(id).getData()));
        return "accesorios/fragments :: view";
    }

    @PostMapping("/accesorios/save")
    public String saveAccesorio(com.sca.model.Accesorio accesorio, Model model) {
        try {
            BindException be = new BindException(accesorio, "accesorio");
            accesorioService.save(accesorio, be);
        } catch (Exception e) {
            // ignore
        }
        model.addAttribute("items", accesorioService.findAll().getData());
        return "accesorios/fragments :: lista";
    }

    @GetMapping("/asociados")
    public String asociadosIndex(Model model) {
        model.addAttribute("title", "Asociados");
        return "asociados/index";
    }

    @GetMapping("/asociados/list")
    public String asociadosList(Model model) {
        model.addAttribute("items", asociadosService.findAll().getData());
        return "asociados/fragments :: lista";
    }

    @GetMapping("/asociados/form")
    public String asociadoForm(Model model) {
    model.addAttribute("asociado", new com.sca.model.Asociados());
    model.addAttribute("categorias", categoriaService.findAll().getData());
    model.addAttribute("firmas", firmaService.findAll().getData());
        return "asociados/fragments :: form";
    }

    @GetMapping("/asociados/{id}")
    public String asociadoById(@PathVariable Long id, Model model) {
    model.addAttribute("asociado", unwrap(asociadosService.findById(id).getData()));
    model.addAttribute("categorias", categoriaService.findAll().getData());
    model.addAttribute("firmas", firmaService.findAll().getData());
        return "asociados/fragments :: form";
    }

    @GetMapping("/asociados/view/{id}")
    public String asociadoViewById(@PathVariable Long id, Model model) {
    model.addAttribute("asociado", unwrap(asociadosService.findById(id).getData()));
        return "asociados/fragments :: view";
    }

    @PostMapping("/asociados/save")
    public String saveAsociado(com.sca.model.Asociados asociado, Model model, HttpServletRequest request) {
        try {
            // Resolve selected categoria IDs (sent as parameter 'categoriaIds') into Categoria objects
            String[] catIds = request.getParameterValues("categoriaIds");
            if (catIds != null) {
                java.util.Set<com.sca.model.Categoria> set = new java.util.HashSet<>();
                for (String sid : catIds) {
                    try {
                        Long cid = Long.parseLong(sid);
                        com.sca.model.Categoria c = new com.sca.model.Categoria();
                        c.setId(cid);
                        set.add(c);
                    } catch (Exception ex) { /* ignore invalid id */ }
                }
                asociado.setCategorias(set);
            }
            // Resolve id_firma if sent as parameter
            String idFirma = request.getParameter("id_firma");
            if (idFirma != null && !idFirma.trim().isEmpty()) {
                try {
                    asociado.setId_firma(Integer.valueOf(idFirma));
                } catch (Exception ex) { }
            }

            BindException be = new BindException(asociado, "asociado");
            asociadosService.save(asociado, be);
        } catch (Exception e) { }
        model.addAttribute("items", asociadosService.findAll().getData());
        return "asociados/fragments :: lista";
    }

    @GetMapping("/asistencias/list")
    public String asistenciasList(Model model) {
        model.addAttribute("items", asistenciaService.findAll().getData());
        return "asistencias/fragments :: lista";
    }

    @GetMapping("/asistencias/form")
    public String asistenciaForm(Model model) {
        model.addAttribute("asistencia", new com.sca.model.Asistencia());
        return "asistencias/fragments :: form";
    }

    @GetMapping("/asistencias/{id}")
    public String asistenciaById(@PathVariable Long id, Model model) {
        model.addAttribute("asistencia", unwrap(asistenciaService.findById(id).getData()));
        return "asistencias/fragments :: form";
    }

    @GetMapping("/asistencias/view/{id}")
    public String asistenciaViewById(@PathVariable Long id, Model model) {
        model.addAttribute("asistencia", unwrap(asistenciaService.findById(id).getData()));
        return "asistencias/fragments :: view";
    }

    @PostMapping("/asistencias/save")
    public String saveAsistencia(com.sca.model.Asistencia asistencia, Model model) {
        try {
            BindException be = new BindException(asistencia, "asistencia");
            asistenciaService.save(asistencia, be);
        } catch (Exception e) { }
        model.addAttribute("items", asistenciaService.findAll().getData());
        return "asistencias/fragments :: lista";
    }

    @GetMapping("/asistencias-total/list")
    public String asistenciasTotalList(Model model) {
        model.addAttribute("items", asistenciaTotalService.findAll().getData());
        return "asistencias-total/fragments :: lista";
    }

    @GetMapping("/asistencias-total/form")
    public String asistenciaTotalForm(Model model) {
        model.addAttribute("asistenciaTotal", new com.sca.model.AsistenciaTotal());
        return "asistencias-total/fragments :: form";
    }

    @GetMapping("/asistencias-total/{id}")
    public String asistenciaTotalById(@PathVariable Long id, Model model) {
        model.addAttribute("asistenciaTotal", unwrap(asistenciaTotalService.findById(id).getData()));
        return "asistencias-total/fragments :: form";
    }

    @GetMapping("/asistencias-total/view/{id}")
    public String asistenciaTotalViewById(@PathVariable Long id, Model model) {
        model.addAttribute("asistenciaTotal", unwrap(asistenciaTotalService.findById(id).getData()));
        return "asistencias-total/fragments :: view";
    }

    @PostMapping("/asistencias-total/save")
    public String saveAsistenciaTotal(com.sca.model.AsistenciaTotal asistenciaTotal, Model model) {
        try {
            BindException be = new BindException(asistenciaTotal, "asistenciaTotal");
            asistenciaTotalService.save(asistenciaTotal, be);
        } catch (Exception e) { }
        model.addAttribute("items", asistenciaTotalService.findAll().getData());
        return "asistencias-total/fragments :: lista";
    }

    @GetMapping("/asistencias")
    public String asistenciasIndex(Model model) {
        model.addAttribute("title", "Asistencias");
        return "asistencias/index";
    }

    @GetMapping("/asistencias-total")
    public String asistenciasTotalIndex(Model model) {
        model.addAttribute("title", "Asistencia Total");
        return "asistencias-total/index";
    }

    @GetMapping("/condiciones")
    public String condicionesIndex(Model model) {
        model.addAttribute("title", "Condiciones");
        return "condiciones/index";
    }

    @GetMapping("/dias")
    public String diasIndex(Model model) {
        model.addAttribute("title", "Días");
        return "dias/index";
    }

    @GetMapping("/firmas")
    public String firmasIndex(Model model) {
        model.addAttribute("title", "Firmas");
        return "firmas/index";
    }

    @GetMapping("/maduradores")
    public String maduradoresIndex(Model model) {
        model.addAttribute("title", "Maduradores");
        return "maduradores/index";
    }
    
    @GetMapping("/maduradores/list")
    public String maduradoresList(Model model) {
        model.addAttribute("items", maduradorService.findAll().getData());
        return "maduradores/fragments :: lista";
    }

    @GetMapping("/maduradores/form")
    public String maduradorForm(Model model) {
        model.addAttribute("madurador", new com.sca.model.Madurador());
        model.addAttribute("lotes", loteService.findAll().getData());
        return "maduradores/fragments :: form";
    }

    @GetMapping("/maduradores/{id}")
    public String maduradorById(@PathVariable Long id, Model model) {
    model.addAttribute("madurador", unwrap(maduradorService.findById(id).getData()));
        model.addAttribute("lotes", loteService.findAll().getData());
        return "maduradores/fragments :: form";
    }

    @GetMapping("/maduradores/view/{id}")
    public String maduradorViewById(@PathVariable Long id, Model model) {
    model.addAttribute("madurador", unwrap(maduradorService.findById(id).getData()));
        return "maduradores/fragments :: view";
    }

    @PostMapping("/maduradores/save")
    public String saveMadurador(com.sca.model.Madurador madurador, Model model, javax.servlet.http.HttpServletRequest request) {
        try {
            // If binder didn't create nested lote, attempt to read lote.id from request and resolve entity
            if (madurador != null && (madurador.getLote() == null || madurador.getLote().getId() == null)) {
                String loteIdStr = request.getParameter("lote.id");
                if (loteIdStr == null) {
                    loteIdStr = request.getParameter("lote");
                }
                if (loteIdStr != null && !loteIdStr.trim().isEmpty()) {
                    try {
                        Long lid = Long.parseLong(loteIdStr);
                        Object l = loteService.findById(lid).getData();
                        if (l instanceof com.sca.model.Lote) {
                            madurador.setLote((com.sca.model.Lote) l);
                        } else {
                            com.sca.model.Lote temp = new com.sca.model.Lote();
                            temp.setId(lid);
                            madurador.setLote(temp);
                        }
                    } catch (Exception ex) {
                        System.out.println("[DEBUG] failed to resolve lote id='" + loteIdStr + "' : " + ex.getMessage());
                    }
                }
            }

            BindException be = new BindException(madurador, "madurador");
            maduradorService.save(madurador, be);
        } catch (Exception e) {
            // ignore
        }
        model.addAttribute("items", maduradorService.findAll().getData());
        return "maduradores/fragments :: lista";
    }

    @GetMapping("/meses")
    public String mesesIndex(Model model) {
        model.addAttribute("title", "Meses");
        return "meses/index";
    }

    @GetMapping("/meses/list")
    public String mesesList(Model model) {
        model.addAttribute("items", mesService.findAll().getData());
        return "meses/fragments :: lista";
    }

    @GetMapping("/meses/form")
    public String mesForm(Model model) {
        model.addAttribute("mes", new com.sca.model.Mes());
        return "meses/fragments :: form";
    }

    @GetMapping("/meses/{id}")
    public String mesById(@PathVariable Long id, Model model) {
    model.addAttribute("mes", unwrap(mesService.findById(id).getData()));
        return "meses/fragments :: form";
    }

    @GetMapping("/meses/view/{id}")
    public String mesViewById(@PathVariable Long id, Model model) {
    model.addAttribute("mes", unwrap(mesService.findById(id).getData()));
        return "meses/fragments :: view";
    }

    @PostMapping("/meses/save")
    public String saveMes(com.sca.model.Mes mes, Model model) {
        try {
            BindException be = new BindException(mes, "mes");
            mesService.save(mes, be);
        } catch (Exception e) { }
        model.addAttribute("items", mesService.findAll().getData());
        return "meses/fragments :: lista";
    }

    @GetMapping("/porcentajes-mes")
    public String porcentajesMesIndex(Model model) {
        model.addAttribute("title", "Porcentajes Mes");
        return "porcentajes-mes/index";
    }

    @GetMapping("/porcentajes-mes/list")
    public String porcentajesMesList(Model model) {
        model.addAttribute("items", porcentajeMesService.findAll().getData());
        return "porcentajes-mes/fragments :: lista";
    }

    @GetMapping("/porcentajes-mes/form")
    public String porcentajeForm(Model model) {
    model.addAttribute("porcentaje", new com.sca.model.PorcentajeMes());
    model.addAttribute("meses", mesService.findAll().getData());
    return "porcentajes-mes/fragments :: form";
    }

    @GetMapping("/porcentajes-mes/{id}")
    public String porcentajeById(@PathVariable Long id, Model model) {
    model.addAttribute("porcentaje", unwrap(porcentajeMesService.findById(id).getData()));
    model.addAttribute("meses", mesService.findAll().getData());
    return "porcentajes-mes/fragments :: form";
    }

    @GetMapping("/porcentajes-mes/view/{id}")
    public String porcentajeViewById(@PathVariable Long id, Model model) {
    model.addAttribute("porcentaje", unwrap(porcentajeMesService.findById(id).getData()));
        return "porcentajes-mes/fragments :: view";
    }

    @PostMapping("/porcentajes-mes/save")
    public String savePorcentaje(com.sca.model.PorcentajeMes porcentaje, Model model, HttpServletRequest request) {
        try {
            // Resolve Mes nested entity when binder did not populate
            if (porcentaje != null && porcentaje.getMes() != null && porcentaje.getMes().getId() != 0) {
                try {
                    Object m = mesService.findById(porcentaje.getMes().getId()).getData();
                    if (m instanceof com.sca.model.Mes) {
                        porcentaje.setMes((com.sca.model.Mes) m);
                    }
                } catch (Exception ex) { }
            }
            // Fallback: check request parameters 'mes.id' or 'mes'
            if (porcentaje != null && (porcentaje.getMes() == null || porcentaje.getMes().getId() == 0)) {
                String mesId = request.getParameter("mes.id");
                if (mesId == null) mesId = request.getParameter("mes");
                if (mesId != null && !mesId.trim().isEmpty()) {
                    try {
                        Long mid = Long.parseLong(mesId);
                        Object m = mesService.findById(mid).getData();
                        if (m instanceof com.sca.model.Mes) {
                            porcentaje.setMes((com.sca.model.Mes) m);
                        } else {
                            com.sca.model.Mes tmp = new com.sca.model.Mes();
                            tmp.setId(mid);
                            porcentaje.setMes(tmp);
                        }
                    } catch (Exception ex) { }
                }
            }
            BindException be = new BindException(porcentaje, "porcentaje");
            porcentajeMesService.save(porcentaje, be);
        } catch (Exception e) { }
        model.addAttribute("items", porcentajeMesService.findAll().getData());
        return "porcentajes-mes/fragments :: lista";
    }

    @GetMapping("/sueldos-basicos")
    public String sueldosBasicosIndex(Model model) {
        model.addAttribute("title", "Sueldos Básicos");
        return "sueldos-basicos/index";
    }

    @GetMapping("/sueldos-basicos/list")
    public String sueldosBasicosList(Model model) {
        model.addAttribute("items", sueldoBasicoService.findAll().getData());
        return "sueldos-basicos/fragments :: lista";
    }

    @GetMapping("/sueldos-basicos/form")
    public String sueldoForm(Model model) {
    model.addAttribute("sueldo", new com.sca.model.SueldoBasico());
    model.addAttribute("categorias", categoriaService.findAll().getData());
    model.addAttribute("porcentajes", porcentajeMesService.findAll().getData());
    return "sueldos-basicos/fragments :: form";
    }

    @GetMapping("/sueldos-basicos/{id}")
    public String sueldoById(@PathVariable Long id, Model model) {
    model.addAttribute("sueldo", sueldoBasicoService.findById(id).getData());
    model.addAttribute("categorias", categoriaService.findAll().getData());
    model.addAttribute("porcentajes", porcentajeMesService.findAll().getData());
    return "sueldos-basicos/fragments :: form";
    }

    @GetMapping("/sueldos-basicos/view/{id}")
    public String sueldoViewById(@PathVariable Long id, Model model) {
        model.addAttribute("sueldo", sueldoBasicoService.findById(id).getData());
        return "sueldos-basicos/fragments :: view";
    }

    @PostMapping("/sueldos-basicos/save")
    public String saveSueldo(com.sca.model.SueldoBasico sueldo, Model model, HttpServletRequest request) {
        try {
            // Resolve nested Categoria and PorcentajeMes if binder did not populate
            if (sueldo != null && sueldo.getCategoria() != null && sueldo.getCategoria().getId() != 0) {
                try {
                    Object c = categoriaService.findById(sueldo.getCategoria().getId()).getData();
                    if (c instanceof com.sca.model.Categoria) {
                        sueldo.setCategoria((com.sca.model.Categoria) c);
                    }
                } catch (Exception ex) { }
            }
            if (sueldo != null && sueldo.getPorcentajeMes() != null && sueldo.getPorcentajeMes().getId() != 0) {
                try {
                    Object p = porcentajeMesService.findById(sueldo.getPorcentajeMes().getId()).getData();
                    if (p instanceof com.sca.model.PorcentajeMes) {
                        sueldo.setPorcentajeMes((com.sca.model.PorcentajeMes) p);
                    }
                } catch (Exception ex) { }
            }
            // Fallback: read request parameters 'categoria.id' and 'porcentajeMes.id'
            if (sueldo != null && (sueldo.getCategoria() == null || sueldo.getCategoria().getId() == 0)) {
                String cid = request.getParameter("categoria.id");
                if (cid == null) cid = request.getParameter("categoria");
                if (cid != null && !cid.trim().isEmpty()) {
                    try {
                        Long idc = Long.parseLong(cid);
                        Object c = categoriaService.findById(idc).getData();
                        if (c instanceof com.sca.model.Categoria) {
                            sueldo.setCategoria((com.sca.model.Categoria) c);
                        } else {
                            com.sca.model.Categoria tmp = new com.sca.model.Categoria(); tmp.setId(idc); sueldo.setCategoria(tmp);
                        }
                    } catch (Exception ex) { }
                }
            }
            if (sueldo != null && (sueldo.getPorcentajeMes() == null || sueldo.getPorcentajeMes().getId() == 0)) {
                String pid = request.getParameter("porcentajeMes.id");
                if (pid == null) pid = request.getParameter("porcentajeMes");
                if (pid != null && !pid.trim().isEmpty()) {
                    try {
                        Long idp = Long.parseLong(pid);
                        Object p = porcentajeMesService.findById(idp).getData();
                        if (p instanceof com.sca.model.PorcentajeMes) {
                            sueldo.setPorcentajeMes((com.sca.model.PorcentajeMes) p);
                        } else {
                            com.sca.model.PorcentajeMes tmp = new com.sca.model.PorcentajeMes(); tmp.setId(idp); sueldo.setPorcentajeMes(tmp);
                        }
                    } catch (Exception ex) { }
                }
            }

            BindException be = new BindException(sueldo, "sueldo");
            sueldoBasicoService.save(sueldo, be);
        } catch (Exception e) { }
        model.addAttribute("items", sueldoBasicoService.findAll().getData());
        return "sueldos-basicos/fragments :: lista";
    }

}
