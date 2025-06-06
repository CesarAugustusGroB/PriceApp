# Ansible

El playbook `deploy.yml` aplica los manifiestos de Kubernetes utilizando el módulo `kubernetes.core.k8s`. Define un host `kube` en tu inventario y proporciona la ruta al `kubeconfig` mediante la variable `kubeconfig`.

```bash
ansible-playbook -i inventory deploy.yml -e kubeconfig=~/.kube/config
```
