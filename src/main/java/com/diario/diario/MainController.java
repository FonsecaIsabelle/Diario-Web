package com.diario.diario;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diario.diario.model.Pasta;
import com.diario.diario.model.PastaService;
import com.diario.diario.model.Post;
import com.diario.diario.model.PostService;
import com.diario.diario.model.Usuario;
import com.diario.diario.model.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PostService postService;

    @Autowired
    PastaService pastaService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/cadastro")
    public String cadastro(){
        return "cadastro";
    }

    @PostMapping("/cadastrar")
    public String cadastrarUsuario(Usuario usuario) {
        usuarioService.inserirUsuario(usuario);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/diario")
    public String diario(Model model, HttpSession session,
                        @RequestParam(required = false) Integer pastaId,
                        @RequestParam(required = false) String data) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        List<Post> posts;
        
        // Filtrar por pasta
        if (pastaId != null && pastaId > 0) {
            posts = postService.listarPostsPorPasta(pastaId);
        }
        // Filtrar por data
        else if (data != null && !data.isEmpty()) {
            posts = postService.listarPostsPorData(usuario.getId(), data);
        }
        // Listar todos
        else {
            posts = postService.listarPostsPorUsuario(usuario.getId());
        }
        
        List<Pasta> pastas = pastaService.listarPastasPorUsuario(usuario.getId());
        
        model.addAttribute("posts", posts);
        model.addAttribute("pastas", pastas);
        model.addAttribute("usuario", usuario);
        model.addAttribute("pastaIdSelecionada", pastaId);
        model.addAttribute("dataSelecionada", data);
        
        return "diario";
    }

    @PostMapping("/autenticar")
    public String autenticar(String email, String senha, Model model, HttpSession session) {
        Usuario usuario = usuarioService.autenticar(email, senha);
        
        if (usuario != null) {
            session.setAttribute("usuarioLogado", usuario);
            return "redirect:/diario";
        } else {
            model.addAttribute("erro", "Email ou senha incorretos");
            return "login";
        }
    }

    @PostMapping("/criarPost")
    public String criarPost(Post post, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        post.setUsuarioId(usuario.getId());
        postService.inserirPost(post);
        
        return "redirect:/diario";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/";
    }

    @GetMapping("/editarPost/{id}")
    public String editarPost(@PathVariable int id, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Post post = postService.buscarPostPorId(id);
        
        if (post == null || post.getUsuarioId() != usuario.getId()) {
            return "redirect:/diario";
        }
        
        model.addAttribute("post", post);
        List<Post> posts = postService.listarPostsPorUsuario(usuario.getId());
        List<Pasta> pastas = pastaService.listarPastasPorUsuario(usuario.getId());
        model.addAttribute("posts", posts);
        model.addAttribute("pastas", pastas);
        model.addAttribute("usuario", usuario);
        model.addAttribute("editando", true);
        
        return "diario";
    }

    @PostMapping("/atualizarPost")
    public String atualizarPost(Post post, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Post postExistente = postService.buscarPostPorId(post.getId());
        
        if (postExistente == null || postExistente.getUsuarioId() != usuario.getId()) {
            return "redirect:/diario";
        }
        
        post.setUsuarioId(usuario.getId());
        postService.atualizarPost(post);
        
        return "redirect:/diario";
    }

    @GetMapping("/deletarPost/{id}")
    public String deletarPost(@PathVariable int id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Post post = postService.buscarPostPorId(id);
        
        if (post == null || post.getUsuarioId() != usuario.getId()) {
            return "redirect:/diario";
        }
        
        postService.deletarPost(id);
        
        return "redirect:/diario";
    }

    @PostMapping("/criarPasta")
    public String criarPasta(Pasta pasta, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        pasta.setUsuarioId(usuario.getId());
        pastaService.inserirPasta(pasta);
        
        return "redirect:/diario";
    }

    @GetMapping("/deletarPasta/{id}")
    public String deletarPasta(@PathVariable int id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Pasta pasta = pastaService.buscarPastaPorId(id);
        
        if (pasta == null || pasta.getUsuarioId() != usuario.getId()) {
            return "redirect:/diario";
        }
        
        pastaService.deletarPasta(id);
        
        return "redirect:/diario";
    }

    @PostMapping("/api/cadastro")
@ResponseBody
public ResponseEntity<?> cadastrarUsuarioComValidacao(@RequestBody Map<String, String> dados) {
    
    String nome = dados.get("nome");
    String email = dados.get("email");
    String senha = dados.get("senha");
    String confirmarSenha = dados.get("confirmarSenha");
    
    // Validar nome
    if (nome == null || nome.trim().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of(
            "sucesso", false,
            "erro", "Nome é obrigatório"
        ));
    }
    
    // Validar email
    if (email == null || email.trim().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of(
            "sucesso", false,
            "erro", "Email é obrigatório"
        ));
    }
    
    if (!usuarioService.validarFormatoEmail(email)) {
        return ResponseEntity.badRequest().body(Map.of(
            "sucesso", false,
            "erro", "Use um email válido (@gmail.com, @hotmail.com, @outlook.com, @yahoo.com ou @icloud.com)"
        ));
    }

    Optional<Usuario> usuarioExistente = usuarioService.buscarPorEmail(email);
    if (usuarioExistente.isPresent()) {
        return ResponseEntity.badRequest().body(Map.of(
            "sucesso", false,
            "erro", "Este email já está cadastrado"
        ));
    }
    
    // Validar senha
    if (senha == null || senha.trim().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of(
            "sucesso", false,
            "erro", "Senha é obrigatória"
        ));
    }

    if (!usuarioService.validarForcaSenha(senha)) {
        return ResponseEntity.badRequest().body(Map.of(
            "sucesso", false,
            "erro", "A senha deve ter no mínimo 6 caracteres, incluindo maiúscula, minúscula e número"
        ));
    }

    if (!senha.equals(confirmarSenha)) {
        return ResponseEntity.badRequest().body(Map.of(
            "sucesso", false,
            "erro", "As senhas não coincidem"
        ));
    }
    
    try {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha);
        
        Usuario usuarioCadastrado = usuarioService.salvar(novoUsuario);
        
        return ResponseEntity.ok(Map.of(
            "sucesso", true,
            "mensagem", "Usuário cadastrado com sucesso!",
            "email", usuarioCadastrado.getEmail()
        ));
        
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body(Map.of(
            "sucesso", false,
            "erro", "Erro ao cadastrar usuário. Tente novamente."
        ));
    }
}    

    @GetMapping("/api/verificar-email")
    @ResponseBody
    public ResponseEntity<?> verificarEmailDisponivel(@RequestParam String email) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        
        return ResponseEntity.ok(Map.of(
            "existe", usuario.isPresent(),
            "mensagem", usuario.isPresent() ? "Este email já está cadastrado" : "Email disponível"
        ));
    }
}
    