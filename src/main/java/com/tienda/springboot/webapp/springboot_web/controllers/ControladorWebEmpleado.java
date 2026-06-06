package com.tienda.springboot.webapp.springboot_web.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tienda.springboot.webapp.springboot_web.entities.Empleado;
import com.tienda.springboot.webapp.springboot_web.repositories.RepositorioEmpleado;
import com.tienda.springboot.webapp.springboot_web.repositories.RepositorioRol;

@Controller
@RequestMapping("/empleados")
public class ControladorWebEmpleado {

    @Autowired
    private RepositorioEmpleado repositorioEmpleado;

    @Autowired
    private RepositorioRol repositorioRol;

    @GetMapping
    public String listarEmpleados(Model model) {
        List<Empleado> empleados = repositorioEmpleado.findAll();
        model.addAttribute("empleados", empleados);
        // Cargar lista de roles
        model.addAttribute("roles", repositorioRol.findAll());
        return "pages/empleados";
    }

    @GetMapping("/consultar/{id}")
    public String ConsultarEmpleado(@PathVariable Integer id, Model model) {
        Optional<Empleado> Empleado = repositorioEmpleado.findById(id);
        if (Empleado.isPresent()) {
            model.addAttribute("empleado", Empleado.get());
            model.addAttribute("roles", repositorioRol.findAll());
            return "pages/empleados"; // Acá va la modal de modo edición
        } else {
            return "redirect:/empleados";
        }
    }

    @PostMapping("/crear")
    public String crearEmpleado(@ModelAttribute Empleado Empleado, RedirectAttributes redirectAttributes) {
        repositorioEmpleado.save(Empleado);
        redirectAttributes.addFlashAttribute("mensaje", "Empleado creado exitosamente");
        return "redirect:/empleados";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarEmpleado(@PathVariable Integer id, @ModelAttribute Empleado detalleEmpleado, RedirectAttributes redirectAttributes) {
        Optional<Empleado> Empleado = repositorioEmpleado.findById(id);
        if (Empleado.isPresent()) {
            Empleado emp = Empleado.get();
            emp.setNombres(detalleEmpleado.getNombres());
            emp.setApellidos(detalleEmpleado.getApellidos());
            emp.setTipoDocumento(detalleEmpleado.getTipoDocumento());
            emp.setNumeroDocumento(detalleEmpleado.getNumeroDocumento());
            emp.setIdRol(detalleEmpleado.getIdRol());
            repositorioEmpleado.save(emp);
            redirectAttributes.addFlashAttribute("mensaje", "Empleado actualizado exitosamente");
        }
        return "redirect:/empleados";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        if(repositorioEmpleado.existsById(id)) {
            repositorioEmpleado.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Empleado eliminado exitosamente");
        }
        return "redirect:/empleados";
    }
}
