package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.domain.entity.User;
import com.yo.yoprj.domain.enums.NotificationRecipientType;
import com.yo.yoprj.domain.enums.UserRole;
import com.yo.yoprj.dto.parent.ParentDashboardResponse;
import com.yo.yoprj.repository.NotificationRepository;
import com.yo.yoprj.repository.TuitionInvoiceRepository;
import com.yo.yoprj.service.AuthService;
import com.yo.yoprj.service.ParentPortalService;
import com.yo.yoprj.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentPortalServiceImpl implements ParentPortalService {

    private final AuthService authService;
    private final StudentService studentService;
    private final TuitionInvoiceRepository tuitionInvoiceRepository;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public ParentDashboardResponse getDashboard(String username) throws BadRequestException {
        User user = authService.findActiveUserByUsername(username);
        if (user.getRole() != UserRole.PARENT || user.getParent() == null) {
            throw new BadRequestException("Current user is not a parent account");
        }

        Integer parentId = user.getParent().getId();
        List<ParentDashboardResponse.StudentCard> students = studentService.findByParentId(parentId).stream()
                .map(s -> new ParentDashboardResponse.StudentCard(
                        s.id(), s.studentCode(), s.fullName(), s.status(), s.latestScore()))
                .toList();

        List<ParentDashboardResponse.InvoiceCard> invoices = tuitionInvoiceRepository.findByStudentParentId(parentId).stream()
                .map(i -> new ParentDashboardResponse.InvoiceCard(
                        i.getId(),
                        i.getInvoiceCode(),
                        i.getStudent().getFullName(),
                        i.getCourseClass().getName(),
                        i.getBillingMonth(),
                        i.getFinalAmount(),
                        i.getAmountPaid(),
                        i.getBalanceAmount(),
                        i.getStatus().name(),
                        i.getDueDate()
                ))
                .toList();

        List<ParentDashboardResponse.NotificationCard> notifications = notificationRepository
                .findByRecipientTypeAndRecipientRefIdOrderByCreatedAtDesc(NotificationRecipientType.PARENT, parentId)
                .stream()
                .map(n -> new ParentDashboardResponse.NotificationCard(
                        n.getId(),
                        n.getType().name(),
                        n.getTitle(),
                        n.getContent(),
                        n.getIsRead(),
                        n.getCreatedAt()
                ))
                .toList();

        return new ParentDashboardResponse(
                parentId,
                user.getParent().getFullName(),
                user.getUsername(),
                students,
                invoices,
                notifications
        );
    }
}

