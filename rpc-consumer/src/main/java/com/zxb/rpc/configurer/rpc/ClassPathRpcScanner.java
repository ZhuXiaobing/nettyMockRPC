package com.zxb.rpc.configurer.rpc;

import com.zxb.rpc.annotation.RpcServiceConsumer;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 *
 */
public class ClassPathRpcScanner extends ClassPathBeanDefinitionScanner{

    private RpcFactoryBean<?> rpcFactoryBean = new RpcFactoryBean<Object>();

    private Class<? extends Annotation> annotationClass = RpcServiceConsumer.class;

    public ClassPathRpcScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("No RPC mapper was found in '"
                    + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    public void registerFilters() {
        addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            if (((ScannedGenericBeanDefinition) holder.getBeanDefinition()).getMetadata().hasAnnotation(annotationClass.getName())) {
                definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
                definition.setBeanClass(this.rpcFactoryBean.getClass());
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                System.out.println(holder);
            }
        }
    }

    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface()
                && beanDefinition.getMetadata().isIndependent()
                && !beanDefinition.getMetadata().isAnnotation();
    }
}
