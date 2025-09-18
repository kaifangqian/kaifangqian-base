<script>
import { defineComponent } from 'vue';
import PaasForm from '@/paas-form.vue';

export default defineComponent({
  name: 'ServeDev',
  components: {
    PaasForm
  }
});
</script>

<template>
  <div id="app">
    <paas-form />
  </div>
</template>
