# basic_stress_test
oc login
oc delete project stress
cd basic_stress_test/stress-svc/deploy

oc new-project stress
oc create -f service.yaml
oc create -f imagestream.yaml
oc create -f build.yaml
oc create -f dc.yaml
oc create -f route.yaml

http://stress-svc-stress.<cluster_domain>/prime/
