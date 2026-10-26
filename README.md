# AdotaPet

Aplicativo Android feito como projeto acadêmico para ajudar na divulgação de animais para adoção e de animais perdidos.

**Tema:** 11. Adoção e Resgate de Animais Comunitários

## Integrantes

- Leonardo Sadi Bazi Sassi
- Evelyn Vitória Cavalheiro dos Santos
- Lucas Magro

## Funcionalidades

- Listar animais e buscar por nome, espécie ou local.
- Filtrar os registros por adoção ou animais perdidos.
- Cadastrar, editar, ver detalhes e excluir animais.
- Informar porte, vacinação, castração, data do último avistamento e um link de foto.
- Registrar interesse em adoção com dados básicos para triagem.
- Marcar animais como em processo de adoção, adotados ou encontrados e consultar o histórico.
- Ver algumas informações sobre os registros na tela Gestão.
- Consultar raças e fotos usando a API pública Dog CEO.
- Escolher tema claro ou escuro. A preferência fica salva no aparelho.

O projeto é um MVP acadêmico. Os dados dos animais ficam salvos localmente no Room. O campo de foto recebe um link, não faz upload de arquivos.

## Tecnologias usadas

- Kotlin
- Jetpack Compose e Material 3
- Navigation Compose
- MVVM com ViewModel e StateFlow
- Repository
- Room para os dados locais
- DataStore para preferências
- Retrofit e Gson para a API de raças
- Coroutines e Flow

## Organização do código

- `data/local`: banco Room, entidade, DAO e dados iniciais.
- `data/remote`: configuração da API.
- `data/preferences`: preferências do aplicativo.
- `data/repository`: acesso aos dados.
- `model`: modelos e funções auxiliares.
- `ui`: telas, componentes, tema e navegação.
- `viewmodel`: estados e ações das telas.

A interface foi feita com Jetpack Compose. As telas recebem os dados e chamam as ações dos ViewModels; os repositórios fazem a comunicação com o banco e com a API.

## Como executar

1. Instale o Android Studio e o JDK 17.
2. Abra a pasta do projeto no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Execute o aplicativo em um emulador ou celular Android.

A tela de raças precisa de internet. Para executar os testes unitários, use `./gradlew test` no terminal do projeto.

