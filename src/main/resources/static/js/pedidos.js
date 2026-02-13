/* pedidos.js - completo y fusionado
   - Usa los endpoints REST si existen en tu backend.
   - Si no usas prefijo, dejar API = ''.
*/
const API = ''; // si tu API está bajo prefijo (ej: '/api'), setéalo aquí

/* Global state */
let accesoriosList = [];
let cervezasList = [];
let barrilesList = [];
let clientesList = [];
let asociadosList = [];

const $ = sel => document.querySelector(sel);
const $$ = sel => Array.from(document.querySelectorAll(sel));

/* Mensajes */
function mensaje(text, type='') {
  const el = $('#msg');
  if (!el) return;
  if (!text) { el.innerHTML = ''; return; }
  const cls = type === 'error' ? 'error' : 'success';
  el.innerHTML = `<div class="${cls}">${text}</div>`;
  setTimeout(() => { if (el) el.innerHTML = ''; }, 6000);
}

/* Util - formateo de fechas */
function formatDateFromMillis(ms) {
  if (ms === null || ms === undefined || ms === '') return '';
  const d = new Date(Number(ms));
  if (isNaN(d)) return '';
  const dd = String(d.getDate()).padStart(2,'0');
  const mm = String(d.getMonth()+1).padStart(2,'0');
  const yyyy = d.getFullYear();
  return `${dd}/${mm}/${yyyy}`;
}
function isoDateInputFromMillis(ms) {
  if (ms === null || ms === undefined || ms === '') return '';
  const d = new Date(Number(ms));
  if (isNaN(d)) return '';
  return d.toISOString().slice(0,10);
}

/* Fetch wrapper */
async function fetchJSON(url, opts) {
  try {
    const res = await fetch(url, Object.assign({ headers: { 'Accept': 'application/json' } }, opts));
    const text = await res.text();
    if (!res.ok) {
      throw new Error(`HTTP ${res.status} - ${text || res.statusText}`);
    }
    if (!text) return null;
    return JSON.parse(text);
  } catch (err) {
    throw err;
  }
}

/* ==== CARGA INICIAL ==== */
async function loadAll() {
  try {

    mensaje('Cargando barriles...');
    const res = await fetchJSON(`${API}/getAllBarril`);
    const data = (res && res.data) ? res.data : (Array.isArray(res) ? res : []);
    
    barrilesList = data.filter(b => !b.estado || String(b.estado).toLowerCase() === 'disponible');
    barrilesList = barrilesList.filter((v,i,a) => a.findIndex(x => x.id === v.id) === i);

    populateSelects();
    mensaje('Barriles cargados.');
  } catch (err) {
    console.error(err);
    mensaje('Error cargando barriles: ' + (err.message || err), 'error');
    mensaje('Cargando datos...');
    const endpoints = [
      `${API}/getAllPedido`,
      `${API}/getAllCerveza`,
      `${API}/getAllAccesorio`,
      `${API}/getAllBarril`,
      `${API}/getAllCliente`,
      `${API}/getAllAsociados`
    ];
    const results = await Promise.all(endpoints.map(u => fetchJSON(u).catch(e => ({ error: e.message }))));

    // Mapear resultados con tolerancia a formas distintas
    const pedRes = results[0];
    const cerRes = results[1];
    const accRes = results[2];
    const barRes = results[3];
    const cliRes = results[4];
    const asoRes = results[5];

    const pedidos = (pedRes && pedRes.data) ? pedRes.data : (Array.isArray(pedRes) ? pedRes : []);
    cervezasList = (cerRes && cerRes.data) ? cerRes.data : (Array.isArray(cerRes) ? cerRes : []);
    accesoriosList = (accRes && accRes.data) ? accRes.data : (Array.isArray(accRes) ? accRes : []);
    barrilesList = (barRes && barRes.data) ? barRes.data : (Array.isArray(barRes) ? barRes : []);
    clientesList = (cliRes && cliRes.data) ? cliRes.data : (Array.isArray(cliRes) ? cliRes : []);
    asociadosList = (asoRes && asoRes.data) ? asoRes.data : (Array.isArray(asoRes) ? asoRes : []);

    // filtrar por disponibles (si la entidad tiene 'estado')
    accesoriosList = accesoriosList.filter(a => !a.estado || String(a.estado).toLowerCase() === 'disponible');
    // dedupe por id
    accesoriosList = accesoriosList.filter((v,i,a) => a.findIndex(x => x.id === v.id) === i);
    barrilesList = barrilesList.filter(b => !b.estado || String(b.estado).toLowerCase() === 'disponible');
    barrilesList = barrilesList.filter((v,i,a) => a.findIndex(x => x.id === v.id) === i);

    // Guardamos globalmente
    window._allPedidos = pedidos;

    populateSelects();
    renderAccesorios();
    renderPedidosTable(pedidos);

    mensaje('', '');
  } catch (err) {
    console.error(err);
    mensaje('Error cargando datos: ' + (err.message || err), 'error');
  }
}

/* POBLAR SELECTS */
function populateSelects() {
  // cervezas
  const cervezaSel = $('#cerveza');
  if (cervezaSel) {
    cervezaSel.innerHTML = '<option value="">-- Elegir cerveza --</option>';
    cervezasList.forEach(c => {
      const price = (c.precioPorLitro !== undefined && c.precioPorLitro !== null) ? c.precioPorLitro : '';
      const label = (c.nombreCerveza || c.nombre || `Cerveza ${c.id}`) + (price !== '' ? ` - $${price}` : '');
      const opt = document.createElement('option');
      opt.value = c.id;
      opt.textContent = label;
      opt.dataset.price = price;
      cervezaSel.appendChild(opt);
    });
  }

  // barriles
  const barrilSel = $('#barril');
  if (barrilSel) {
    barrilSel.innerHTML = '<option value="">-- Elegir barril --</option>';
    barrilesList.forEach(b => {
      const text = `#${b.id} - ${b.litros != null ? b.litros + ' L' : ''} ${b.notas ? '- ' + b.notas : ''}`;
      const opt = document.createElement('option');
      opt.value = b.id;
      opt.textContent = text;
      opt.dataset.litros = b.litros != null ? b.litros : 0;
      barrilSel.appendChild(opt);
    });
  }

  // clientes
  const clienteSel = $('#cliente');
  if (clienteSel) {
    clienteSel.innerHTML = '<option value="">-- Elegir cliente --</option>';
    clientesList.forEach(c => {
      const name = ((c.nombre||'') + (c.apellido ? ' ' + c.apellido : '')).trim();
      const opt = document.createElement('option');
      opt.value = c.id;
      opt.textContent = `${name || 'Cliente ' + c.id} ${c.documento ? '('+c.documento+')' : ''}`;
      opt.dataset.documento = c.documento || '';
      clienteSel.appendChild(opt);
    });
  }

  // asociados
  const asoSel = $('#asociado');
  if (asoSel) {
    asoSel.innerHTML = '<option value="">-- Elegir usuario --</option>';
    asociadosList.forEach(a => {
      const name = ((a.nombre||'') + (a.apellido ? ' ' + a.apellido : '')).trim();
      const opt = document.createElement('option');
      opt.value = a.id;
      opt.textContent = `${name || 'Asociado ' + a.id} ${a.legajo ? '('+a.legajo+')' : ''}`;
      asoSel.appendChild(opt);
    });
  }

  updateCervezaInfo();
  updateBarrilInfo();
}

/* RENDER ACCESORIOS (checkbox + precio input) */
function renderAccesorios() {
  const cont = $('#accesoriosContainer');
  if (!cont) return;
  cont.innerHTML = '';
  accesoriosList.forEach(acc => {
    const wrapper = document.createElement('div');
    wrapper.className = 'd-flex align-items-center gap-2 mb-1';

    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.id = `acc_chk_${acc.id}`;
    checkbox.dataset.id = acc.id;

    const label = document.createElement('label');
    label.htmlFor = checkbox.id;
    label.style.marginRight = '8px';
    label.textContent = `${acc.nombre || 'Accesorio ' + acc.id} ${acc.notas ? '- ' + acc.notas : ''}`;

    const priceInput = document.createElement('input');
    priceInput.type = 'number';
    priceInput.step = '0.01';
    priceInput.placeholder = 'Precio (opcional)';
    priceInput.className = 'form-control form-control-sm acc-price';
    priceInput.style.width = '140px';
    priceInput.dataset.id = acc.id;
    priceInput.disabled = true;

    checkbox.addEventListener('change', () => {
      priceInput.disabled = !checkbox.checked;
      if (!checkbox.checked) priceInput.value = '';
      recalcTotal();
    });
    priceInput.addEventListener('input', recalcTotal);

    wrapper.appendChild(checkbox);
    wrapper.appendChild(label);
    wrapper.appendChild(priceInput);
    cont.appendChild(wrapper);
  });
}

/* AGREGAR DETALLE (producto / accesorio) */
function agregarDetalle(tipo, id, cantidad = 1) {
  if (!id) { mensaje('Seleccioná un artículo para agregar', 'error'); return; }

  // obtener datos según tipo
  if (tipo === 'Cerveza') {
    const cer = cervezasList.find(c => String(c.id) === String(id));
    if (!cer) { mensaje('Cerveza no encontrada', 'error'); return; }
    // tomar un barril seleccionado para litros, si hay
    const barrilId = $('#barril').value;
    const barril = barrilesList.find(b => String(b.id) === String(barrilId));
    const litrosPorBarril = barril ? Number(barril.litros || 0) : 0;
    const qty = Number($('#cantidad').value) || Number(cantidad);
    const precioPorLitro = Number(cer.precioPorLitro || 0);
    const litrosTotales = litrosPorBarril * qty;
    const subtotal = Math.round((precioPorLitro * litrosTotales + Number.EPSILON) * 100) / 100;

    const tr = document.createElement('tr');
    tr.dataset.tipo = 'cerveza';
    tr.dataset.id = cer.id;
    tr.innerHTML = `
      <td>Cerveza</td>
      <td>${cer.nombreCerveza || cer.nombre || 'Cerveza '+cer.id}</td>
      <td class="litros">${litrosPorBarril || ''}</td>
      <td class="qty">${qty}</td>
      <td class="unit">${precioPorLitro || 0}</td>
      <td class="subtotal">${subtotal}</td>
      <td class="nowrap">
        <button type="button" class="btn btn-sm btn-danger btn-remove">Quitar</button>
      </td>
    `;
    $('#detalleTable tbody').appendChild(tr);
  } else if (tipo === 'Accesorio') {
    const acc = accesoriosList.find(a => String(a.id) === String(id));
    if (!acc) { mensaje('Accesorio no encontrado', 'error'); return; }
    // si hay precio en el input asociado, usarlo
    const priceInput = document.querySelector(`.acc-price[data-id="${id}"]`);
    const precio = priceInput && priceInput.value ? Number(priceInput.value) : (acc.precio || 0);
    const qty = 1;
    const subtotal = Math.round((precio * qty + Number.EPSILON) * 100) / 100;
    const tr = document.createElement('tr');
    tr.dataset.tipo = 'accesorio';
    tr.dataset.id = acc.id;
    tr.innerHTML = `
      <td>Accesorio</td>
      <td>${acc.nombre || 'Accesorio '+acc.id}</td>
      <td class="litros">-</td>
      <td class="qty">${qty}</td>
      <td class="unit">${precio || 0}</td>
      <td class="subtotal">${subtotal}</td>
      <td class="nowrap">
        <button type="button" class="btn btn-sm btn-danger btn-remove">Quitar</button>
      </td>
    `;
    $('#detalleTable tbody').appendChild(tr);
  }

  // agregar listener al botón quitar
  document.querySelectorAll('#detalleTable .btn-remove').forEach(btn => {
    btn.onclick = (e) => {
      const tr = e.target.closest('tr');
      if (tr) tr.remove();
      recalcTotal();
    };
  });

  recalcTotal();
}

/* RECALCULAR TOTALES */
function recalcTotal() {
  let totalCervezas = 0;
  let totalAccesorios = 0;
  let totalGeneral = 0;

  $$('#detalleTable tbody tr').forEach(tr => {
    const tipo = tr.dataset.tipo;
    const subtotal = Number(tr.querySelector('.subtotal').textContent || 0);
    if (tipo === 'cerveza') {
      totalCervezas += subtotal;
    } else {
      totalAccesorios += subtotal;
    }
  });

  totalGeneral = Math.round((totalCervezas + totalAccesorios + Number.EPSILON) * 100) / 100;
  $('#totalCervezas').value = totalCervezas !== 0 ? ('$' + totalCervezas) : '';
  $('#totalAccesorios').value = totalAccesorios !== 0 ? ('$' + totalAccesorios) : '';
  $('#totalGral').value = totalGeneral !== 0 ? ('$' + totalGeneral) : '';
  return totalGeneral;
}

/* UPDATE displays for cerveza / barril */
function updateCervezaInfo() {
  const v = $('#cerveza').value;
  const span = $('#cervezaPrecio');
  if (!v) { if (span) span.textContent = '-'; return; }
  const c = cervezasList.find(x => String(x.id) === String(v));
  if (span) span.textContent = c && (c.precioPorLitro != null) ? ('$' + c.precioPorLitro) : '-';
  recalcTotal();
}
function updateBarrilInfo() {
  const v = $('#barril').value;
  const span = $('#barrilLitros');
  if (!v) { if (span) span.textContent = '-'; return; }
  const b = barrilesList.find(x => String(x.id) === String(v));
  if (span) span.textContent = b && (b.litros != null) ? (b.litros + ' L') : '-';
  recalcTotal();
}

/* RESET form */
function resetForm() {
  const form = $('#pedidoForm');
  form.reset();
  $('#pedidoId').value = '';
  $('#direccionEntrega').disabled = true;
  $('#direccionEntrega').value = 'Sin envío';
  $('#cantidad').value = 1;
  $('#detalleTable tbody').innerHTML = '';
  $('#totalCervezas').value = '';
  $('#totalAccesorios').value = '';
  $('#totalGral').value = '';
  $('#nota').value = '';
  // deshabilitar precios de accesorios
  $$('.acc-price').forEach(inp => { inp.value=''; inp.disabled = true; });
  // fecha pedido por defecto hoy
  setDefaultFecha();
  mensaje('', '');
}

/* CARGAR UN PEDIDO EN FORM PARA EDITAR */
async function loadPedidoIntoForm(id) {
  try {
    mensaje('Cargando pedido...');
    const res = await fetchJSON(`${API}/getByIdPedido/${id}`);
    const p = (res && res.data) ? res.data : res;
    if (!p) { mensaje('Pedido no encontrado', 'error'); return; }

    $('#pedidoId').value = p.id || '';
    $('#fechaPedido').value = isoDateInputFromMillis(p.fechaPedido);
    $('#fechaEntrega').value = isoDateInputFromMillis(p.fechaEntrega);

    if (p.envio || p.direccionEntrega) {
      $('#envio').checked = Boolean(p.envio);
      $('#direccionEntrega').disabled = false;
      $('#direccionEntrega').value = p.direccionEntrega || '';
    } else {
      $('#envio').checked = false;
      $('#direccionEntrega').disabled = true;
      $('#direccionEntrega').value = 'Sin envío';
    }

    // cliente
    const clienteId = (p.cliente && p.cliente.id) ? p.cliente.id : (p.cliente || '');
    $('#cliente').value = clienteId;

    // usuario/asociado
    const u = p.usuario || p.asociado || null;
    if (u) {
      $('#asociado').value = (typeof u === 'object') ? (u.id || '') : u;
    } else {
      $('#asociado').value = '';
    }

    // limpiar detalle y reconstruir desde p.cervezas, p.barriles, p.accesorios o esquema que retorne
    $('#detalleTable tbody').innerHTML = '';
    // cervezas: si viene array
    if (p.cervezas && Array.isArray(p.cervezas)) {
      p.cervezas.forEach(c => {
        // buscar cerveza en catálogo para precio, nombre
        const cerCat = cervezasList.find(x => String(x.id) === String(c.id));
        // si p incluye cantidad/barril info, respetarla; si no, usar 1
        const qty = (c.cantidad || 1);
        // tomar primer barril si hay
        const barrObj = (p.barriles && Array.isArray(p.barriles) && p.barriles[0]) ? p.barriles[0] : null;
        const litrosPorBarril = barrObj ? (barrObj.litros || 0) : ( (barrilesList[0] && barrilesList[0].litros) || 0 );
        const precioPorLitro = (cerCat && cerCat.precioPorLitro) ? Number(cerCat.precioPorLitro) : (c.precioPorLitro || 0);
        const litrosTotales = litrosPorBarril * qty;
        const subtotal = Math.round((precioPorLitro * litrosTotales + Number.EPSILON) * 100) / 100;
        const tr = document.createElement('tr');
        tr.dataset.tipo = 'cerveza';
        tr.dataset.id = c.id;
        tr.innerHTML = `
          <td>Cerveza</td>
          <td>${cerCat ? (cerCat.nombreCerveza || cerCat.nombre) : (c.nombreCerveza || c.nombre || c.id)}</td>
          <td class="litros">${litrosPorBarril}</td>
          <td class="qty">${qty}</td>
          <td class="unit">${precioPorLitro}</td>
          <td class="subtotal">${subtotal}</td>
          <td class="nowrap"><button type="button" class="btn btn-sm btn-danger btn-remove">Quitar</button></td>
        `;
        $('#detalleTable tbody').appendChild(tr);
      });
    }

    // accesorios
    const accIds = (p.accesorios || []).map(a => (a.id !== undefined ? String(a.id) : String(a)));
    $$('.acc-price').forEach(inp => {
      const id = inp.dataset.id;
      const chk = document.querySelector(`#acc_chk_${id}`);
      if (chk) {
        if (accIds.includes(String(id))) {
          chk.checked = true;
          inp.disabled = false;
          // no sabemos precio guardado en backend: lo dejamos vacío para que el usuario lo ponga si desea
        } else {
          chk.checked = false;
          inp.disabled = true;
          inp.value = '';
        }
      }
    });

    // estado / pago
    $('#estado').value = p.estado || 'Pendiente';
    $('#estadoPago').checked = (p.estadoPago === 'Pagado' || p.estadoPago === true);

    // totalGral
    $('#totalGral').value = p.totalGral != null ? ('$' + p.totalGral) : recalcTotal();
    $('#nota').value = p.nota || '';

    // listeners for remove buttons
    document.querySelectorAll('#detalleTable .btn-remove').forEach(btn => {
      btn.onclick = (e) => {
        e.target.closest('tr').remove();
        recalcTotal();
      };
    });

    mensaje('', '');
  } catch (err) {
    console.error(err);
    mensaje('Error cargando pedido: ' + (err.message || err), 'error');
  }
}

/* GUARDAR PEDIDO (create/update) */
async function submitFormHandler(e) {
  e.preventDefault();
  try {
    // Validaciones mínimas
    const fechaP = $('#fechaPedido').value;
    const fechaE = $('#fechaEntrega').value;
    if (!fechaP) { mensaje('Fecha de pedido obligatoria', 'error'); return; }
    if (fechaE && new Date(fechaE) <= new Date(fechaP)) { mensaje('La fecha de entrega debe ser posterior a la fecha de pedido', 'error'); return; }

    const clienteId = $('#cliente').value;
    const usuarioId = $('#asociado').value;
    if (!clienteId || !usuarioId) { mensaje('Cliente y Usuario obligatorios', 'error'); return; }

    // preparar accesorios seleccionados
    const accesoriosSeleccionados = [];
    $$('.acc-price').forEach(inp => {
      const id = inp.dataset.id;
      const chk = document.querySelector(`#acc_chk_${id}`);
      if (chk && chk.checked) {
        // si usuario puso precio en input lo usamos, sino usamos posible precio del catálogo
        const precio = inp.value ? Number(inp.value) : ( (accesoriosList.find(a => String(a.id) === String(id))||{}).precio || 0 );
        accesoriosSeleccionados.push({ id: Number(id), precio });
      }
    });

    // detalle cervezas (del detalleTable)
    const cervezasPayload = [];
    const barrilesPayload = [];
    $$('#detalleTable tbody tr').forEach(tr => {
      const tipo = tr.dataset.tipo;
      const id = tr.dataset.id;
      const qty = Number(tr.querySelector('.qty').textContent || 1);
      if (tipo === 'cerveza') {
        cervezasPayload.push({ id: Number(id), cantidad: qty });
      } else if (tipo === 'accesorio') {
        accesoriosSeleccionados.push({ id: Number(id) });
      }
    });

    // total calculado
    const total = recalcTotal() || 0;

    const envio = $('#envio').checked;
    const direccion = envio ? ($('#direccionEntrega').value.trim() || 'Sin envío') : 'Sin envío';

    const payload = {
      id: $('#pedidoId').value ? Number($('#pedidoId').value) : 0,
      fechaPedido: new Date($('#fechaPedido').value).getTime(),
      fechaEntrega: $('#fechaEntrega').value ? new Date($('#fechaEntrega').value).getTime() : null,
      direccionEntrega: direccion,
      estado: $('#estado').value || 'Pendiente',
      estadoPago: $('#estadoPago').checked ? 'Pagado' : 'Pendiente',
      envio: envio,
      accesorios: accesoriosSeleccionados.map(a => ({ id: Number(a.id) })),
      cervezas: cervezasPayload,
      barriles: ( $('#barril').value ? [{ id: Number($('#barril').value) }] : [] ),
      usuario: { id: Number($('#asociado').value) },
      asociado: { id: Number($('#asociado').value) },
      cliente: { id: Number($('#cliente').value) },
      totalGral: total,
      nota: $('#nota').value || ''
    };

    const isNew = !$('#pedidoId').value || Number($('#pedidoId').value) === 0;
    const url = isNew ? `${API}/addPedido` : `${API}/updatePedido`;
    const method = isNew ? 'POST' : 'PUT';

    mensaje('Guardando pedido...');
    const res = await fetchJSON(url, {
      method,
      headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
      body: JSON.stringify(payload)
    });

    const ok = res && (res.codigo === '200' || res.codigo === '201' || /ok/i.test(res.status) || res.estado === 'OK');
    if (ok) {
      mensaje('Pedido guardado correctamente.');
      await loadAll();
      resetForm();
    } else {
      const txt = (res && (res.descripcion || res.message || res.error)) ? (res.descripcion || res.message || res.error) : 'No se pudo guardar el pedido';
      mensaje(txt, 'error');
    }
  } catch (err) {
    console.error(err);
    mensaje('Error al guardar: ' + (err.message || err), 'error');
  }
}

/* CANCELAR PEDIDO */
async function cancelPedido(id) {
  if (!confirm('¿Cancelar el pedido #' + id + '?')) return;
  try {
    const res = await fetchJSON(`${API}/cancelPedido/${id}`, { method: 'PUT' });
    const ok = res && (res.codigo === '200' || /ok/i.test(res.status));
    if (ok) {
      mensaje('Pedido cancelado correctamente.');
      await loadAll();
    } else {
      mensaje('No se pudo cancelar: ' + (res.descripcion || res.message || ''), 'error');
    }
  } catch (err) {
    console.error(err);
    mensaje('Error al cancelar: ' + (err.message || err), 'error');
  }
}

/* ELIMINAR PEDIDO (usa DELETE - funcionalidad de la nueva versión) */
async function deletePedido(id) {
  if (!confirm('¿Eliminar el pedido #' + id + '? Esta acción es irreversible.')) return;
  try {
    const res = await fetchJSON(`${API}/deletePedido/${id}`, { method: 'DELETE' });
    const ok = res && (res.codigo === '200' || /ok/i.test(res.status));
    if (ok) {
      mensaje('Pedido eliminado correctamente.');
      await loadAll();
    } else {
      mensaje('No se pudo eliminar: ' + (res.descripcion || res.message || ''), 'error');
    }
  } catch (err) {
    console.error(err);
    mensaje('Error al eliminar: ' + (err.message || err), 'error');
  }
}

/* RENDER TABLA DE PEDIDOS */
function renderPedidosTable(items) {
  const tbody = $('#pedidosTableBody');
  if (!tbody) return;
  tbody.innerHTML = '';
  if (!Array.isArray(items)) items = [];

  items.forEach(p => {
    const tr = document.createElement('tr');

    // cliente nombre
    let clienteNombre = '';
    if (p.cliente) {
      if (typeof p.cliente === 'object') {
        clienteNombre = ((p.cliente.nombre||'') + ' ' + (p.cliente.apellido||'')).trim();
      } else {
        const found = clientesList.find(c => String(c.id) === String(p.cliente));
        clienteNombre = found ? ((found.nombre||'') + ' ' + (found.apellido||'')).trim() : ('ID:' + p.cliente);
      }
    }

    // asociado
    let asociadoNombre = '';
    const uField = p.usuario || p.asociado || null;
    if (uField) {
      if (typeof uField === 'object') {
        asociadoNombre = ((uField.nombre||'') + ' ' + (uField.apellido||'')).trim();
      } else {
        const f = asociadosList.find(a => String(a.id) === String(uField));
        asociadoNombre = f ? ((f.nombre||'') + ' ' + (f.apellido||'')).trim() : ('ID:' + uField);
      }
    }

    // cerveza principal (primera)
    let cervezaNombre = '';
    if (p.cervezas && Array.isArray(p.cervezas) && p.cervezas.length > 0) {
      const c = p.cervezas[0];
      if (typeof c === 'object') cervezaNombre = c.nombreCerveza || c.nombre || String(c.id || '');
      else {
        const f = cervezasList.find(x => String(x.id) === String(c));
        cervezaNombre = f ? (f.nombreCerveza || f.nombre) : ('ID:' + c);
      }
    }

    // barril principal (primero)
    let barrilNombre = '';
    if (p.barriles && Array.isArray(p.barriles) && p.barriles.length > 0) {
      const b = p.barriles[0];
      if (typeof b === 'object') barrilNombre = `#${b.id} ${b.litros ? '- ' + b.litros + ' L' : ''}`;
      else {
        const f = barrilesList.find(x => String(x.id) === String(b));
        barrilNombre = f ? (`#${f.id} ${f.litros ? '- ' + f.litros + ' L' : ''}`) : ('ID:' + b);
      }
    }

    // accesorios names
    let accesoriosTxt = '';
    if (p.accesorios && Array.isArray(p.accesorios)) {
      accesoriosTxt = p.accesorios.map(a => {
        if (typeof a === 'object') return a.nombre || ('ID:' + a.id);
        const found = accesoriosList.find(x => String(x.id) === String(a));
        return found ? found.nombre : ('ID:' + a);
      }).join(', ');
    }

    const cantidad = (p.cervezas && Array.isArray(p.cervezas) && p.cervezas[0] && p.cervezas[0].cantidad) ? p.cervezas[0].cantidad : (p.cantidad || '');

    const fechaP = formatDateFromMillis(p.fechaPedido);
    const fechaE = formatDateFromMillis(p.fechaEntrega);
    const totalTxt = p.totalGral != null ? ('$' + p.totalGral) : '';

    tr.innerHTML = `
      <td class="nowrap">${p.id}</td>
      <td>${clienteNombre}</td>
      <td>${asociadoNombre}</td>
      <td>${cervezaNombre}</td>
      <td>${barrilNombre}</td>
      <td>${accesoriosTxt}</td>
      <td>${cantidad}</td>
      <td>${fechaP}</td>
      <td>${fechaE}</td>
      <td>${p.estado || ''}</td>
      <td>${p.direccionEntrega && p.direccionEntrega !== 'Sin envío' ? p.direccionEntrega : 'Sin envío'}</td>
      <td>${totalTxt}</td>
      <td class="nowrap">
        <button class="btn btn-sm btn-warning me-1" data-action="edit" data-id="${p.id}">Editar</button>
        <button class="btn btn-sm btn-secondary me-1" data-action="cancel" data-id="${p.id}">Cancelar</button>
        <button class="btn btn-sm btn-danger" data-action="delete" data-id="${p.id}">Eliminar</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

/* Manejo click acciones en tabla (delegación) */
function pedidosTableClickHandler(e) {
  const btn = e.target.closest('button');
  if (!btn) return;
  const action = btn.dataset.action;
  const id = btn.dataset.id;
  if (!action || !id) return;
  if (action === 'edit') {
    loadPedidoIntoForm(id);
  } else if (action === 'cancel') {
    cancelPedido(id);
  } else if (action === 'delete') {
    deletePedido(id);
  }
}

/* FILTRADO simple por inputs (cliente, dni, estado) */
function filtrarPedidos() {
  let pedidos = (window._allPedidos && Array.isArray(window._allPedidos)) ? window._allPedidos.slice() : [];
  const clienteQ = ($('#filtroCliente').value || '').trim().toLowerCase();
  const dniQ = ($('#filtroDni').value || '').trim().toLowerCase();
  const estadoQ = ($('#filtroEstado').value || '').trim();

  if (clienteQ) {
    pedidos = pedidos.filter(p => {
      if (!p.cliente) return false;
      if (typeof p.cliente === 'object') {
        const name = ((p.cliente.nombre||'') + ' ' + (p.cliente.apellido||'')).toLowerCase();
        return name.includes(clienteQ);
      } else {
        const found = clientesList.find(c => String(c.id) === String(p.cliente));
        if (found) {
          const name = ((found.nombre||'') + ' ' + (found.apellido||'')).toLowerCase();
          return name.includes(clienteQ);
        }
        return false;
      }
    });
  }

  if (dniQ) {
    pedidos = pedidos.filter(p => {
      const doc = (p.cliente && p.cliente.documento) ? String(p.cliente.documento).toLowerCase() : '';
      if (!doc) {
        // intentar buscar en clientesList
        const found = clientesList.find(c => String(c.id) === String((p.cliente && p.cliente.id) ? p.cliente.id : p.cliente));
        return found && String(found.documento || '').toLowerCase().includes(dniQ);
      }
      return doc.includes(dniQ);
    });
  }

  if (estadoQ) {
    pedidos = pedidos.filter(p => (p.estado || '').toLowerCase() === estadoQ.toLowerCase());
  }

  if (!pedidos.length) {
    mensaje('No se encontraron pedidos con los filtros indicados', 'error');
  } else {
    mensaje('', '');
  }

  renderPedidosTable(pedidos);
}

/* Setear fecha por defecto al cargar form */
function setDefaultFecha() {
  const hoy = new Date();
  const yyyy = hoy.getFullYear();
  const mm = String(hoy.getMonth() + 1).padStart(2, '0');
  const dd = String(hoy.getDate()).padStart(2, '0');
  const iso = `${yyyy}-${mm}-${dd}`;
  $('#fechaPedido').value = iso;
}

/* Low stock alerts (código adaptado de versión vieja) */
(function lowStockModule(){
  const DEFAULT_THRESHOLDS = { cervezas: 100, barriles: 10, lotes: 5, maduradores: 2 };
  const STORAGE_KEY = 'lowstock_thresholds';
  const ENDPOINTS = {
    cervezas: ['/reports/cervezas/check-threshold?threshold={t}', '/checkStockCerveza?threshold={t}'],
    barriles: ['/reports/barriles/check-threshold?threshold={t}', '/checkStockBarril?threshold={t}'],
    lotes: ['/reports/lotes/check-threshold?threshold={t}', '/reports/lotes/check-threshold?threshold={t}'],
    maduradores: ['/reports/maduradores/check-threshold?threshold={t}', '/finByEstado/Activo?threshold={t}']
  };

  function loadThresholds() {
    try {
      const s = localStorage.getItem(STORAGE_KEY);
      return s ? Object.assign({}, DEFAULT_THRESHOLDS, JSON.parse(s)) : Object.assign({}, DEFAULT_THRESHOLDS);
    } catch(e) { return Object.assign({}, DEFAULT_THRESHOLDS); }
  }
  function escapeHtml(unsafe) {
    if (unsafe == null) return '';
    return String(unsafe).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")
      .replace(/"/g,"&quot;").replace(/'/g,"&#039;");
  }
  async function safeParse(resp) {
    const ct = resp.headers.get('content-type') || '';
    try {
      if (ct.includes('application/json')) {
        const obj = await resp.json();
        if (typeof obj === 'string') return obj;
        if (obj.descripcion) return obj.descripcion;
        if (obj.message) return obj.message;
        if (obj.data && typeof obj.data === 'string') return obj.data;
        if (obj.data && Array.isArray(obj.data)) {
          return obj.data.length === 0 ? 'OK' : JSON.stringify(obj.data);
        }
        return JSON.stringify(obj);
      } else {
        return await resp.text();
      }
    } catch(e){
      try { return await resp.text(); } catch(_) { return String(e); }
    }
  }
  async function callCheckEndpoint(url) {
    try {
      const r = await fetch(url);
      const msg = await safeParse(r);
      return { ok: r.ok, message: msg, status: r.status };
    } catch (e) {
      return { ok: false, message: e.message || String(e) };
    }
  }
  async function tryEndpointsForCategory(category, threshold) {
    const list = ENDPOINTS[category] || [];
    if (!list.length) return { ok: false, message: 'No hay endpoints configurados' };
    let last = null;
    for (const pattern of list) {
      const url = pattern.replace('{t}', encodeURIComponent(threshold));
      last = await callCheckEndpoint(url);
      if (last.ok) return last;
    }
    return last;
  }
  function isBelowText(msg) {
    if (!msg) return false;
    const s = (typeof msg === 'string') ? msg : String(msg);
    return /por debajo|⚠|baj[oa]|insuficient|no alcanza|below|insufficient/i.test(s);
  }
  function capitalize(s){ return s.charAt(0).toUpperCase()+s.slice(1); }
  function formatMessage(category, res) {
    if (!res) return `<li class="text-danger">Error (${capitalize(category)}): respuesta vacía</li>`;
    if (res.ok) {
      if (isBelowText(res.message)) {
        return `<li><span style="color:#c0392b">🚨 ${capitalize(category)} Insuficientes</span></li>`;
      } else {
        return `<li>✔ ${capitalize(category)} Suficientes</li>`;
      }
    } else {
      const msg = (typeof res.message === 'object') ? (res.message.descripcion || res.message.message || JSON.stringify(res.message)) : res.message;
      return `<li class="text-danger">⚠ ${capitalize(category)}: ${escapeHtml(msg)}</li>`;
    }
  }

  async function loadLowStock() {
    // buscar contenedor: home usa lowstock-content, sino usamos #alertas container si existe
    const contentEl = document.getElementById('lowstock-content') || document.getElementById('alertas-container') || null;
    if (!contentEl) return;
    contentEl.innerHTML = '<p class="small text-muted mb-0">Actualizando alertas...</p>';
    const thresholds = loadThresholds();
    const categories = ['cervezas','barriles','lotes','maduradores'];
    const items = [];
    for (const cat of categories) {
      const thr = thresholds[cat] !== undefined ? thresholds[cat] : DEFAULT_THRESHOLDS[cat];
      const res = await tryEndpointsForCategory(cat, thr);
      items.push(formatMessage(cat, res));
    }
    contentEl.innerHTML = `<ul class="mb-0">${items.join('\n')}</ul>`;
  }

  document.addEventListener('DOMContentLoaded', function(){
    // cargar alerts al inicio y cada 60s
    loadLowStock();
    setInterval(loadLowStock, 60000);
  });
})();

/* LISTENERS & INIT */
function attachListeners() {
  // form submit
  const form = $('#pedidoForm');
  if (form) form.addEventListener('submit', submitFormHandler);

  // reset
  const resetBtn = $('#resetBtn');
  if (resetBtn) resetBtn.addEventListener('click', resetForm);

  // agregar producto (usa cerveza+barril+cantidad)
  const btnAdd = $('#btnAgregarProducto');
  if (btnAdd) btnAdd.addEventListener('click', () => {
    const cerId = $('#cerveza').value;
    agregarDetalle('Cerveza', cerId, Number($('#cantidad').value || 1));
  });

  // agregar accesorio desde select (si lo querés usar)
  const accSel = $('#accesorio');
  if (accSel) {
    // si incluís select de accesorios en el HTML, podrías manejarlo. Lo dejamos inactivo si no existe.
    accSel.addEventListener('change', () => {});
  }

  // cambios en selects para mostrar info
  const cervezaSel = $('#cerveza');
  if (cervezaSel) cervezaSel.addEventListener('change', updateCervezaInfo);
  const barrilSel = $('#barril');
  if (barrilSel) barrilSel.addEventListener('change', updateBarrilInfo);

  // envio toggle
  const envioInput = $('#envio');
  if (envioInput) {
    envioInput.addEventListener('change', () => {
      if (envioInput.checked) {
        $('#direccionEntrega').disabled = false;
        $('#direccionEntrega').value = '';
        $('#direccionEntrega').required = true;
      } else {
        $('#direccionEntrega').disabled = true;
        $('#direccionEntrega').value = 'Sin envío';
        $('#direccionEntrega').required = false;
      }
    });
  }

  // tabla actions
  const tbody = $('#pedidosTable');
  if (tbody) tbody.addEventListener('click', pedidosTableClickHandler);

  // filtros
  const btnFiltrar = $('#btnFiltrar');
  if (btnFiltrar) btnFiltrar.addEventListener('click', filtrarPedidos);
  const showAllBtn = $('#showAllBtn');
  if (showAllBtn) showAllBtn.addEventListener('click', () => {
    renderPedidosTable(window._allPedidos || []);
    $('#filtroCliente').value = '';
    $('#filtroDni').value = '';
    $('#filtroEstado').value = '';
  });

  // eliminar desde detalle table (delegado)
  document.addEventListener('click', function(e){
    if (e.target && e.target.matches('#detalleTable .btn-remove')) {
      const tr = e.target.closest('tr');
      if (tr) tr.remove();
      recalcTotal();
    }
  });
}

document.addEventListener("DOMContentLoaded", () => {
  const cervezaSel = document.getElementById("cerveza");
  const barrilSel = document.getElementById("barril");

  if (cervezaSel && barrilSel) {
    cervezaSel.addEventListener("change", async function () {
      const cervezaId = this.value;
      barrilSel.innerHTML = '<option value="">-- Elegir barril --</option>';

      if (!cervezaId) return;

      try {
        const res = await fetch(`/barriles/byCerveza/${cervezaId}`);
        if (!res.ok) throw new Error("Error al buscar barriles");
        const barriles = await res.json();

        barriles.forEach(b => {
          const opt = document.createElement("option");
          opt.value = b.id;
          opt.textContent = `#${b.id} - ${b.litros}L ${b.notas ? "- " + b.notas : ""}`;
          barrilSel.appendChild(opt);
        });
      } catch (err) {
        console.error("Error cargando barriles:", err);
      }
    });
  }
});


document.addEventListener('DOMContentLoaded', async () => {
  setDefaultFecha();
  attachListeners();
  await loadAll();
});
