// validacao-cadastro.js

document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form[action="/cadastrar"]');
    if (!form) return;

    const nomeInput = document.getElementById('nome');
    const emailInput = document.getElementById('email');
    const senhaInput = document.getElementById('senha');
    const confirmarSenhaInput = document.getElementById('confirmarSenha');
    
    const nomeError = document.getElementById('nomeError');
    const emailError = document.getElementById('emailError');
    const senhaError = document.getElementById('senhaError');
    const confirmarSenhaError = document.getElementById('confirmarSenhaError');

    emailInput.addEventListener('blur', async function() {
        const email = this.value.trim();
        emailError.textContent = '';
        emailInput.classList.remove('input-error');

        if (!email) {
            emailError.textContent = 'Email é obrigatório';
            emailInput.classList.add('input-error');
            return;
        }

        if (!validarFormatoEmail(email)) {
            emailError.textContent = 'Use um email válido (@gmail.com, @hotmail.com, @outlook.com, @yahoo.com ou @icloud.com)';
            emailInput.classList.add('input-error');
            return;
        }

        try {
            const response = await fetch(`/api/verificar-email?email=${encodeURIComponent(email)}`);
            const data = await response.json();
            
            if (data.existe) {
                emailError.textContent = 'Este email já está cadastrado';
                emailInput.classList.add('input-error');
            }
        } catch (error) {
            console.error('Erro ao verificar email:', error);
        }
    });

    senhaInput.addEventListener('input', function() {
        const senha = this.value;
        senhaError.textContent = '';
        senhaInput.classList.remove('input-error');

        if (senha && !validarForcaSenha(senha)) {
            senhaError.textContent = 'A senha deve ter no mínimo 6 caracteres, incluindo maiúscula, minúscula e número';
            senhaInput.classList.add('input-error');
        }

        if (confirmarSenhaInput.value) {
            validarConfirmacaoSenha();
        }
    });

    confirmarSenhaInput.addEventListener('input', validarConfirmacaoSenha);

    function validarConfirmacaoSenha() {
        confirmarSenhaError.textContent = '';
        confirmarSenhaInput.classList.remove('input-error');
        
        if (confirmarSenhaInput.value && senhaInput.value !== confirmarSenhaInput.value) {
            confirmarSenhaError.textContent = 'As senhas não coincidem';
            confirmarSenhaInput.classList.add('input-error');
        }
    }

    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        nomeError.textContent = '';
        emailError.textContent = '';
        senhaError.textContent = '';
        confirmarSenhaError.textContent = '';
        
        nomeInput.classList.remove('input-error');
        emailInput.classList.remove('input-error');
        senhaInput.classList.remove('input-error');
        confirmarSenhaInput.classList.remove('input-error');

        let temErro = false;

        const nome = nomeInput.value.trim();
        if (!nome) {
            nomeError.textContent = 'Nome é obrigatório';
            nomeInput.classList.add('input-error');
            temErro = true;
        }

        const email = emailInput.value.trim();
        if (!email) {
            emailError.textContent = 'Email é obrigatório';
            emailInput.classList.add('input-error');
            temErro = true;
        } else if (!validarFormatoEmail(email)) {
            emailError.textContent = 'Use um email válido (@gmail.com, @hotmail.com, @outlook.com, @yahoo.com ou @icloud.com)';
            emailInput.classList.add('input-error');
            temErro = true;
        }

        const senha = senhaInput.value;
        if (!senha) {
            senhaError.textContent = 'Senha é obrigatória';
            senhaInput.classList.add('input-error');
            temErro = true;
        } else if (!validarForcaSenha(senha)) {
            senhaError.textContent = 'A senha deve ter no mínimo 6 caracteres, incluindo maiúscula, minúscula e número';
            senhaInput.classList.add('input-error');
            temErro = true;
        }

        const confirmarSenha = confirmarSenhaInput.value;
        if (!confirmarSenha) {
            confirmarSenhaError.textContent = 'Confirme sua senha';
            confirmarSenhaInput.classList.add('input-error');
            temErro = true;
        } else if (senha !== confirmarSenha) {
            confirmarSenhaError.textContent = 'As senhas não coincidem';
            confirmarSenhaInput.classList.add('input-error');
            temErro = true;
        }

        if (temErro) {
            return;
        }

        try {
            const dados = {
                nome: nome,
                email: email,
                senha: senha,
                confirmarSenha: confirmarSenha
            };

            const response = await fetch('/api/cadastro', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dados)
            });

            const resultado = await response.json();

            if (resultado.sucesso) {
                
                window.location.href = '/login?cadastro=sucesso';
            } else {
                
                if (resultado.erro.toLowerCase().includes('email')) {
                    emailError.textContent = resultado.erro;
                    emailInput.classList.add('input-error');
                } else if (resultado.erro.toLowerCase().includes('senha')) {
                    senhaError.textContent = resultado.erro;
                    senhaInput.classList.add('input-error');
                } else if (resultado.erro.toLowerCase().includes('nome')) {
                    nomeError.textContent = resultado.erro;
                    nomeInput.classList.add('input-error');
                } else {
                    alert(resultado.erro);
                }
            }
        } catch (error) {
            console.error('Erro ao cadastrar:', error);
            alert('Erro ao cadastrar usuário. Tente novamente.');
        }
    });
});

function validarFormatoEmail(email) {
    const regex = /^[A-Za-z0-9+_.-]+@(gmail|hotmail|outlook|yahoo|icloud)\.com$/;
    return regex.test(email);
}

function validarForcaSenha(senha) {
    if (senha.length < 6) return false;
    
    const temMaiuscula = /[A-Z]/.test(senha);
    const temMinuscula = /[a-z]/.test(senha);
    const temNumero = /\d/.test(senha);
    
    return temMaiuscula && temMinuscula && temNumero;
}